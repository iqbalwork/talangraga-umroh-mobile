package com.talangraga.data.repository

import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.database.DatabaseHelper
import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.data.local.database.model.TransactionEntity
import com.talangraga.data.local.database.model.UserEntity
import com.talangraga.data.local.session.Session
import com.talangraga.data.local.session.SessionKey
import com.talangraga.data.mapper.toPaymentEntity
import com.talangraga.data.mapper.toPeriodEntity
import com.talangraga.data.mapper.toTransactionEntity
import com.talangraga.data.mapper.toUserEntity
import com.talangraga.data.network.TokenManager
import com.talangraga.data.network.api.ApiService
import com.talangraga.data.network.api.Result
import com.talangraga.data.network.model.response.DataResponse
import com.talangraga.data.network.model.response.TokenResponse
import com.talangraga.data.network.model.response.UserResponse
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class RepositoryImpl(
    private val apiService: ApiService,
    private val json: Json,
    private val session: Session,
    private val tokenManager: TokenManager,
    private val databaseHelper: DatabaseHelper
) : Repository {

    inline fun <T> safeApiCall(
        crossinline apiCall: suspend () -> DataResponse<T>,
        crossinline onSuccess: suspend (T) -> Unit = {}
    ): Flow<Result<T>> = flow {
        try {
            val response = apiCall()
            val data = response.data

            if (data != null) {
                onSuccess(data)
                emit(Result.Success(data))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: JsonConvertException) {
            val message = normalizeErrorMessage(e)
            emit(Result.Error(Exception(message)))
        } catch (e: Exception) {
            val message = normalizeErrorMessage(e)
            emit(Result.Error(Exception(message)))
        }
    }

    fun <LocalType, NetworkType> networkBoundResource(
        query: () -> Flow<LocalType>?,
        fetch: suspend () -> DataResponse<NetworkType>,
        saveFetchResult: suspend (LocalType) -> Unit,
        mapper: (NetworkType) -> LocalType
    ): Flow<Result<LocalType>> = channelFlow {

        val db = launch {
            query()?.collectLatest { data ->
                send(Result.Success(data))
            }
        }

        launch(Dispatchers.IO) {
            try {
                // Fetch new data
                val networkResponse = fetch()
                if (networkResponse.data != null) {
                    val mappedData = mapper(networkResponse.data)
                    // Replace cache
                    saveFetchResult(mappedData)
                    // Emit updated data
                    send(Result.Success(mappedData))
                } else {
                    send(Result.Error(Exception(networkResponse.message)))
                }
            } catch (e: JsonConvertException) {
                val message =
                    normalizeErrorMessage(e)
                send(Result.Error(Exception(message)))
            } catch (e: Exception) {
                val message =
                    normalizeErrorMessage(e)
                send(Result.Error(Exception(message)))
            }
        }

        awaitClose { db.cancel() }
    }

    override fun login(
        identifier: String,
        password: String
    ): Flow<Result<TokenResponse>> {
        return safeApiCall(
            apiCall = {
                tokenManager.clearToken()
                apiService.login(identifier, password)
            },
            onSuccess = { token ->
                tokenManager.saveAccessToken(token.accessToken.orEmpty())
                tokenManager.saveRefreshToken(token.refreshToken.orEmpty())
                token.userResponse?.let {
                    session.saveProfile(token.userResponse)
                    session.saveBoolean(SessionKey.IS_LOGGED_IN, true)
                }
            }
        )
    }

    override fun getLoginProfile(): Flow<Result<UserResponse>> {
        return safeApiCall(
            apiCall = { apiService.getLoginProfile() },
            onSuccess = {
                session.saveProfile(it)
            }
        )
    }

    override fun getListUsers(): Flow<Result<List<UserEntity>>> {
        return networkBoundResource(
            query = { databaseHelper.getAllUsersAsFlow() },
            fetch = { apiService.getListUsers() },
            saveFetchResult = { networkSource ->
                val local = databaseHelper.getAllUsers()
                val networkIds = networkSource.map { it.userId }.toSet()
                val dataToDelete = local
                    .filter { it.userId !in networkIds }
                    .map { it.userId }

                databaseHelper.deleteUserByIds(dataToDelete)
                databaseHelper.insertUsers(networkSource)
            },
            mapper = {
                it.map { userResponse -> userResponse.toUserEntity() }
            }
        )
    }

    override fun getLocalUsers(): Flow<Result<List<UserEntity>>> {
        return flow {
            try {
                val cache = databaseHelper.getAllUsers()
                if (cache.isNotEmpty()) {
                    emit(Result.Success(cache))
                } else {
                    apiService.getListUsers()
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex))
            }
        }
    }

    override fun getPeriods(): Flow<Result<List<PeriodEntity>>> {
        return networkBoundResource(
            query = { databaseHelper.getAllPeriodsAsFlow() },
            fetch = { apiService.getPeriods() },
            saveFetchResult = { networkSource ->
                val local = databaseHelper.getAllPeriods()
                val networkIds = networkSource.map { it.periodId }.toSet()
                val dataToDelete = local
                    .filter { it.periodId !in networkIds }
                    .map { it.periodId }
                databaseHelper.deletePeriodByIds(dataToDelete)
                databaseHelper.insertPeriods(networkSource)
            },
            mapper = {
                it.map { periodeResponse -> periodeResponse.toPeriodEntity() }
            }
        )
    }

    override fun getTransactions(
        periodId: Int?,
        status: String?,
        paymentId: Int?
    ): Flow<Result<List<TransactionEntity>>> {
        return networkBoundResource(
            query = { databaseHelper.getAllTransactionsAsFlow() },
            fetch = { apiService.getTransactions(periodId, status, paymentId) },
            saveFetchResult = { networkSource ->
                val localTransactions = databaseHelper.getAllTransactions()
                val networkTransactionIds = networkSource.map { it.transactionId }.toSet()
                val transactionsToDelete = localTransactions
                    .filter { it.transactionId !in networkTransactionIds }
                    .map { it.transactionId }

                databaseHelper.deleteTransactionByIds(transactionsToDelete)
                databaseHelper.insertTransactions(networkSource)
            },
            mapper = {
                it.map { transactionResponse ->
                    transactionResponse.toTransactionEntity()
                }
            }
        )
    }

    // SQL Delight
    override fun getPayments(): Flow<Result<List<PaymentEntity>>> {
        return networkBoundResource(
            query = { databaseHelper.getAllPaymentsAsFlow() },
            fetch = { apiService.getPayments() },
            saveFetchResult = { networkSource ->
                val local = databaseHelper.getAllPayments()
                val networkIds = networkSource.map { it.paymentId }.toSet()
                val dataToDelete = local
                    .filter { it.paymentId !in networkIds }
                    .map { it.paymentId }

                databaseHelper.deletePaymentByIds(dataToDelete)
                databaseHelper.insertPayments(networkSource)
            },
            mapper = {
                it.map { paymentResponse -> paymentResponse.toPaymentEntity() }
            }
        )
    }

}
