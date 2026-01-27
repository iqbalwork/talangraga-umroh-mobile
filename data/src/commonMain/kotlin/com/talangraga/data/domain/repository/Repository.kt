package com.talangraga.data.domain.repository

import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.data.local.database.model.TransactionEntity
import com.talangraga.data.local.database.model.UserEntity
import com.talangraga.data.network.api.Result
import com.talangraga.data.network.model.response.TokenResponse
import com.talangraga.data.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
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
    fun getUser(userId: Int): Flow<Result<UserEntity>>
    fun getLocalUsers(): Flow<Result<List<UserEntity>>>
    fun registerNewUser(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>>

    fun getLocalUser(userId: Int): Flow<Result<UserEntity>>
    fun updateMe(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>>

    fun updateUser(
        userId: Int,
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>>

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<Result<Unit>>

    fun addTransaction(
        userId: Int?,
        reportedByUserId: Int?,
        amount: Double?,
        transactionDate: String?,
        periodeId: Int?,
        paymentId: Int?,
        file: ByteArray?
    ): Flow<Result<Boolean>>
}
