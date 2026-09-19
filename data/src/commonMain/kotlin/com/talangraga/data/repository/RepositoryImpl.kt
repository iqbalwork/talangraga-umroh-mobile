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
import com.talangraga.data.network.model.response.PeriodeResponse
import com.talangraga.data.network.model.response.TokenResponse
import com.talangraga.data.network.model.response.UserResponse
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class RepositoryImpl(
    private val apiService: ApiService,
    private val session: Session,
    private val tokenManager: TokenManager,
    private val databaseHelper: DatabaseHelper
) : Repository {

    override fun getEmailByIdentifier(identifier: String): Flow<Result<String>> {
        return safeApiCallDirect(
            apiCall = { apiService.getEmailByIdentifier(identifier) }
        )
    }

    override fun login(
        identifier: String,
        password: String
    ): Flow<Result<TokenResponse>> {
        return safeApiCallDirect(
            apiCall = {
                tokenManager.clearToken()
                val token = apiService.login(identifier, password)
                if (token.accessToken.isNullOrBlank()) {
                    throw Exception("Invalid login credentials")
                }
                token
            },
            onSuccess = { token ->
                val accessToken = token.accessToken.orEmpty()
                if (accessToken.isNotBlank()) {
                    tokenManager.saveAccessToken(accessToken)
                    tokenManager.saveRefreshToken(token.refreshToken.orEmpty())
                    session.saveBoolean(SessionKey.IS_LOGGED_IN, true)
                    token.userResponse?.let {
                        session.saveProfile(it)
                    }
                }
            }
        )
    }

    override fun getLoginProfile(): Flow<Result<UserResponse>> {
        val currentUserId = session.getProfile()?.id.orEmpty()
        return safeApiCallDirect(
            apiCall = { apiService.getLoginProfile(currentUserId) },
            onSuccess = {
                session.saveProfile(it)
                databaseHelper.insertUsers(listOf(it.toUserEntity()))
            }
        )
    }

    override fun registerNewUser(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>> {
        return flow {
            try {
                val response = apiService.registerUser(
                    fullname = fullname,
                    username = username,
                    email = email,
                    phone = phone,
                    password = password,
                    domicile = domicile,
                    userType = userType,
                    imageProfile = imageProfile
                )
                val user = response.userResponse ?: if (!response.id.isNullOrBlank()) {
                    UserResponse(
                        id = response.id,
                        rawEmail = email,
                        rawFullname = fullname,
                        rawUsername = username,
                        rawPhone = phone,
                        rawDomisili = domicile,
                        rawUserType = userType
                    )
                } else null
                if (user != null) {
                    databaseHelper.insertUsers(listOf(user.toUserEntity()))
                    emit(Result.Success(user))
                } else {
                    emit(Result.Error(Exception("Registration failed: User not created")))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun updateMe(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>> {
        val userId = session.getProfile()?.id.orEmpty()
        return updateUser(userId, fullname, username, email, phone, password, domicile, userType, imageProfile)
    }

    override fun updateUser(
        userId: String,
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>> {
        return flow {
            try {
                val response = apiService.updateUser(
                    userId = userId,
                    fullname = fullname,
                    phone = phone,
                    domicile = domicile,
                    username = username,
                    userType = userType,
                    imageProfile = imageProfile
                )
                databaseHelper.insertUsers(listOf(response.toUserEntity()))
                if (userId == session.getProfile()?.id) {
                    session.saveProfile(response)
                }
                emit(Result.Success(response))
            } catch (ex: Exception) {
                emit(Result.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getUser(userId: String): Flow<Result<UserEntity>> {
        return channelFlow {
            try {
                databaseHelper.getAllUsersAsFlow()
                    .catch {
                        send(Result.Error(Exception(it.cause)))
                    }
                    .map { list ->
                        list.find { it.userId == userId }
                    }
                    .collectLatest {
                        if (it != null) {
                            send(Result.Success(it))
                        } else {
                            send(Result.Error(Exception("User not found")))
                        }
                    }
            } catch (ex: Exception) {
                send(Result.Error(ex))
            }
        }
    }

    override fun getListUsers(): Flow<Result<List<UserEntity>>> {
        return networkBoundResourceDirect(
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
                    val remote = apiService.getListUsers()
                    databaseHelper.insertUsers(remote.map { it.toUserEntity() })
                    emit(Result.Success(databaseHelper.getAllUsers()))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex))
            }
        }
    }

    override fun getLocalUser(userId: String): Flow<Result<UserEntity>> {
        return flow {
            try {
                databaseHelper.getUserById(userId)
                    .collectLatest {
                        if (it.isNotEmpty()) {
                            emit(Result.Success(it.first()))
                        } else {
                            emit(Result.Error(Exception("User not found")))
                        }
                    }
            } catch (ex: Exception) {
                emit(Result.Error(ex))
            }
        }
    }

    override fun getPeriods(): Flow<Result<List<PeriodEntity>>> {
        return networkBoundResourceDirect(
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

    override fun addPeriode(periodeName: String, startDate: String, endDate: String): Flow<Result<PeriodeResponse>> {
        return safeApiCallDirect(
            apiCall = { apiService.addPeriode(periodeName, startDate, endDate) },
            onSuccess = {
                databaseHelper.insertPeriods(listOf(it.toPeriodEntity()))
            }
        )
    }

    override fun getTransactions(
        periodId: Int?,
        status: String?,
        paymentId: Int?
    ): Flow<Result<List<TransactionEntity>>> {
        return networkBoundResourceDirect(
            query = { databaseHelper.getAllTransactionsAsFlow() },
            fetch = { apiService.getTransactions(periodId, status, paymentId) },
            saveFetchResult = { networkSource ->
                val localTransactions = databaseHelper.getAllTransactions()
                val networkTransactionIds = networkSource.map { it.transactionId }.toSet()
                val transactionsToDelete = localTransactions
                    .filter {
                        (periodId == null || it.periodId == periodId) &&
                                (status == null || it.statusTransaksi == status) &&
                                it.transactionId !in networkTransactionIds
                    }
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

    override fun getPayments(): Flow<Result<List<PaymentEntity>>> {
        return networkBoundResourceDirect(
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

    override fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<Result<Unit>> {
        return flow {
            try {
                apiService.changePassword(newPassword)
                emit(Result.Success(Unit))
            } catch (ex: Exception) {
                emit(Result.Error(ex))
            }
        }
    }

    override fun addTransaction(
        userId: String?,
        reportedByUserId: String?,
        amount: Double?,
        transactionDate: String?,
        periodeId: Int?,
        paymentId: Int?,
        file: ByteArray?
    ): Flow<Result<Boolean>> {
        return flow {
            try {
                val response = apiService.addTransaction(
                    userId = userId,
                    reportedByUserId = reportedByUserId,
                    amount = amount,
                    transactionDate = transactionDate,
                    periodeId = periodeId,
                    paymentId = paymentId,
                    file = file
                )
                databaseHelper.insertTransactions(listOf(response.toTransactionEntity()))
                emit(Result.Success(true))
            } catch (e: JsonConvertException) {
                val message = normalizeErrorMessage(e)
                emit(Result.Error(Exception(message)))
            } catch (e: Exception) {
                val message = normalizeErrorMessage(e)
                emit(Result.Error(Exception(message)))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun updateTransactionStatus(
        transactionId: Int,
        status: String
    ): Flow<Result<TransactionEntity>> {
        val confirmedById = session.getProfile()?.id
        return safeApiCallDirect(
            apiCall = { apiService.updateTransactionStatus(transactionId, status, confirmedById) },
            onSuccess = { transactionResponse ->
                databaseHelper.insertTransactions(listOf(transactionResponse.toTransactionEntity()))
            }
        ).map { result ->
            when (result) {
                is Result.Success -> Result.Success(result.data.toTransactionEntity())
                is Result.Error -> Result.Error(result.t)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun deleteTransaction(
        transactionId: Int
    ): Flow<Result<Unit>> {
        return flow {
            try {
                apiService.deleteTransaction(transactionId)
                databaseHelper.deleteTransactionById(transactionId.toLong())
                emit(Result.Success(Unit))
            } catch (e: JsonConvertException) {
                val message = normalizeErrorMessage(e)
                emit(Result.Error(Exception(message)))
            } catch (e: Exception) {
                val message = normalizeErrorMessage(e)
                emit(Result.Error(Exception(message)))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun exportTransactions(
        periodId: Int?,
        userId: String?,
        status: String?,
        format: String
    ): Flow<Result<ByteArray>> = flow {
        try {
            emit(Result.Success(byteArrayOf()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
