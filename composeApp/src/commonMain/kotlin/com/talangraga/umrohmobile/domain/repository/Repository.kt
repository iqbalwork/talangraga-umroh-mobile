package com.talangraga.umrohmobile.domain.repository

import com.talangraga.umrohmobile.data.local.database.model.PaymentEntity
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.data.network.api.Result
import com.talangraga.umrohmobile.data.network.model.response.TokenResponse
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun login(identifier: String, password: String): Flow<Result<TokenResponse>>
    fun getLoginProfile(): Flow<Result<UserResponse>>
    fun getPeriods(): Flow<Result<List<PeriodEntity>>>
    fun getTransactions(periodId: Int, status: String? = null, paymentId: Int? = null): Flow<Result<List<TransactionEntity>>>
    fun getPayments(): Flow<Result<List<PaymentEntity>>>
    fun getListUsers(): Flow<Result<List<UserEntity>>>
}
