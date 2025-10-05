package com.talangraga.umrohmobile.data.repository

import SessionStore
import com.talangraga.umrohmobile.data.local.database.dao.PaymentDao
import com.talangraga.umrohmobile.data.local.database.dao.PeriodDao
import com.talangraga.umrohmobile.data.local.database.dao.TransactionDao
import com.talangraga.umrohmobile.data.local.database.dao.UserDao
import com.talangraga.umrohmobile.data.local.database.model.PaymentEntity
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.data.local.session.DataStoreKey
import com.talangraga.umrohmobile.data.local.session.TokenManager
import com.talangraga.umrohmobile.data.local.session.saveBoolean
import com.talangraga.umrohmobile.data.local.session.saveString
import com.talangraga.umrohmobile.data.mapper.toPaymentEntity
import com.talangraga.umrohmobile.data.mapper.toPeriodEntity
import com.talangraga.umrohmobile.data.mapper.toTransactionEntity
import com.talangraga.umrohmobile.data.mapper.toUserEntity
import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.data.network.api.AuthService
import com.talangraga.umrohmobile.data.network.api.Result
import com.talangraga.umrohmobile.data.network.model.response.AuthResponse
import com.talangraga.umrohmobile.data.network.model.response.BaseResponse
import com.talangraga.umrohmobile.data.network.model.response.StrapiError
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import com.talangraga.umrohmobile.domain.repository.Repository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
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
    private val authService: AuthService,
    private val json: Json,
    private val sessionStore: SessionStore,
    private val tokenManager: TokenManager,
    private val periodDao: PeriodDao,
    private val transactionDao: TransactionDao,
    private val paymentDao: PaymentDao,
    private val userDao: UserDao
) : Repository {

    private inline fun <reified T : BaseResponse> safeApiCall(crossinline apiCall: suspend () -> T): Flow<ApiResponse<T, BaseResponse>> {
        return flow {
            try {
                val result = apiCall()
                if (result.error != null) {
                    emit(ApiResponse.Error(result))
                } else {
                    emit(ApiResponse.Success(result))
                }
            } catch (e: ClientRequestException) {
                val httpResponse: HttpResponse = e.response
                try {
                    // Attempt to parse the error body into ErrorResponse
                    val errorBody = httpResponse.body<String>()
                    val baseResponse = json.decodeFromString<BaseResponse>(errorBody)
                    emit(ApiResponse.Error(baseResponse))
                } catch (parseException: Exception) {
                    // Fallback if parsing the error body fails or if it's not a JSON error
                    emit(
                        ApiResponse.Error(
                            BaseResponse(
                                error = StrapiError(
                                    status = httpResponse.status.value,
                                    name = "ClientRequestError",
                                    message = parseException.message
                                        ?: "Failed to parse error body",
                                )
                            )
                        )
                    )
                }
            } catch (_: JsonConvertException) {
                emit(
                    ApiResponse.Error(
                        BaseResponse(
                            error = StrapiError(
                                status = 0, // Or a specific status code for general errors
                                name = "GenericError",
                                message = "Serialization failure!",
                            )
                        )
                    )
                )
            } catch (e: Exception) {
                // Catch other exceptions (e.g., network issues, serialization issues not from ClientRequestException)
                emit(
                    ApiResponse.Error(
                        BaseResponse(
                            error = StrapiError(
                                status = 0, // Or a specific status code for general errors
                                name = "GenericError",
                                message = e.message ?: "An unexpected error occurred",
                            )
                        )
                    )
                )
            }
        }
    }

    fun <LocalType, NetworkType> networkBoundResource(
        query: () -> Flow<LocalType>,
        fetch: suspend () -> NetworkType,
        saveFetchResult: suspend (LocalType) -> Unit,
        mapper: (NetworkType) -> LocalType
    ): Flow<Result<LocalType>> = channelFlow {

        val db = launch {
            query().collectLatest { data ->
                send(Result.Success(data))
            }
        }

        launch(Dispatchers.IO) {
            try {
                // Fetch new data
                val networkResponse = fetch()
                val mappedData = mapper(networkResponse)
                // Replace cache
                saveFetchResult(mappedData)
                // Emit updated data
                send(Result.Success(mappedData))
            } catch (e: JsonConvertException) {
                val message = e.message.orEmpty()
                val errorBody = message.substringAfter("JSON input:").trim()
                val baseResponse = json.decodeFromString<BaseResponse>(errorBody)
                val ex = Exception(baseResponse.error?.message.orEmpty())
                send(Result.Error(ex))
            } catch (e: Exception) {
                val message = e.message.orEmpty()
                send(Result.Error(e))
            }
        }

        awaitClose { db.cancel() }
    }

    override fun login(
        identifier: String,
        password: String
    ): Flow<ApiResponse<AuthResponse, BaseResponse>> {
        return safeApiCall {
            val authData = authService.login(identifier, password)
            tokenManager.saveToken(authData.jwt.orEmpty())
            sessionStore.saveBoolean(DataStoreKey.IS_LOGGED_IN, true)
            authData
        }
    }

    override fun getLoginProfile(): Flow<ApiResponse<UserResponse, BaseResponse>> {
        return safeApiCall {
            val apiResponse = authService.getLoginProfile()
            val profileToString = json.encodeToString(UserResponse.serializer(), apiResponse)
            sessionStore.saveString(DataStoreKey.PROFILE_KEY, profileToString)
            apiResponse
        }
    }

    override fun getListUsers(): Flow<Result<List<UserEntity>>> {
        return networkBoundResource(
            query = { userDao.getAllUsers() },
            fetch = { authService.getListUsers() },
            saveFetchResult = { networkSource ->
                val local = userDao.getCachedUsers()
                val networkIds = networkSource.map { it.userId }.toSet()
                val dataToDelete = local
                    .filter { it.userId !in networkIds }
                    .map { it.userId }

                userDao.deleteByIds(dataToDelete)
                userDao.insertUsers(networkSource)
            },
            mapper = {
                it.map { userResponse -> userResponse.toUserEntity() }
            }
        )
    }

    override fun getPeriods(): Flow<Result<List<PeriodEntity>>> {
        return networkBoundResource(
            query = { periodDao.getAllPeriods() },
            fetch = { authService.getPeriods() },
            saveFetchResult = { networkSource ->
                val local = periodDao.getCachedPeriods()
                val networkIds = networkSource.map { it.periodId }.toSet()
                val dataToDelete = local
                    .filter { it.periodId !in networkIds }
                    .map { it.periodId }
                periodDao.deleteByIds(dataToDelete)
                periodDao.inserts(networkSource)
            },
            mapper = {
                it.data?.map { periodeResponse -> periodeResponse.toPeriodEntity() } ?: emptyList()
            }
        )
    }

    override fun getTransactions(periodId: Int): Flow<Result<List<TransactionEntity>>> {
        return networkBoundResource(
            query = { transactionDao.getAllTransactions() },
            fetch = { authService.getTransactions(periodId) },
            saveFetchResult = { networkSource ->
                val localTransactions = transactionDao.getCachedTransactions()
                val networkTransactionIds = networkSource.map { it.transactionId }.toSet()
                val transactionsToDelete = localTransactions
                    .filter { it.transactionId !in networkTransactionIds }
                    .map { it.transactionId }

                transactionDao.deleteTransactionByIds(transactionsToDelete)
                transactionDao.inserts(networkSource)
            },
            mapper = {
                it.data?.map { transactionResponse ->
                    transactionResponse.toTransactionEntity()
                } ?: emptyList()
            }
        )
    }

    override fun getPayments(): Flow<Result<List<PaymentEntity>>> {
        return networkBoundResource(
            query = { paymentDao.getAllPayments() },
            fetch = { authService.getPayments() },
            saveFetchResult = { networkSource ->
                val local = paymentDao.getCachedPayments()
                val networkIds = networkSource.map { it.paymentId }.toSet()
                val dataToDelete = local
                    .filter { it.paymentId !in networkIds }
                    .map { it.paymentId }

                paymentDao.deleteByIds(dataToDelete)
                paymentDao.inserts(networkSource)
            },
            mapper = {
                it.data?.map { paymentResponse -> paymentResponse.toPaymentEntity() } ?: emptyList()
            }
        )
    }

}
