package com.talangraga.data.domain.repository

import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.data.local.database.model.TransactionEntity
import com.talangraga.data.local.database.model.UserEntity
import com.talangraga.data.network.api.Result
import com.talangraga.data.network.model.response.PeriodeResponse
import com.talangraga.data.network.model.response.TokenResponse
import com.talangraga.data.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getEmailByIdentifier(identifier: String): Flow<Result<String>>
    fun login(identifier: String, password: String): Flow<Result<TokenResponse>>
    fun getLoginProfile(): Flow<Result<UserResponse>>
    fun getPeriods(): Flow<Result<List<PeriodEntity>>>
    fun getTransactions(
        periodId: Int? = null,
        status: String? = null,
        paymentId: Int? = null
    ): Flow<Result<List<TransactionEntity>>>

    fun getPayments(): Flow<Result<List<PaymentEntity>>>
    fun getListUsers(): Flow<Result<List<UserEntity>>>
    fun getUser(userId: String): Flow<Result<UserEntity>>
    fun getLocalUsers(): Flow<Result<List<UserEntity>>>
    fun registerNewUser(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray? = null
    ): Flow<Result<UserResponse>>

    fun getLocalUser(userId: String): Flow<Result<UserEntity>>
    fun updateMe(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray? = null
    ): Flow<Result<UserResponse>>

    fun updateUser(
        userId: String,
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray? = null
    ): Flow<Result<UserResponse>>

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<Result<Unit>>

    fun addTransaction(
        userId: String?,
        reportedByUserId: String?,
        amount: Double?,
        transactionDate: String?,
        periodeId: Int?,
        paymentId: Int?,
        file: ByteArray?
    ): Flow<Result<Boolean>>

    fun updateTransactionStatus(
        transactionId: Int,
        status: String
    ): Flow<Result<TransactionEntity>>

    fun deleteTransaction(
        transactionId: Int
    ): Flow<Result<Unit>>

    fun addPeriode(
        periodeName: String,
        startDate: String,
        endDate: String
    ): Flow<Result<PeriodeResponse>>

    fun exportTransactions(
        periodId: Int? = null,
        userId: String? = null,
        status: String? = null,
        format: String = "excel"
    ): Flow<Result<ByteArray>>
}
