package com.talangraga.umrohmobile.domain.repository

import com.talangraga.umrohmobile.data.local.database.model.PaymentEntity
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.data.network.api.Result
import com.talangraga.umrohmobile.data.network.model.response.AuthResponse
import com.talangraga.umrohmobile.data.network.model.response.BaseResponse
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun login(identifier: String, password: String): Flow<ApiResponse<AuthResponse, BaseResponse>>
    fun getLoginProfile(): Flow<ApiResponse<UserResponse, BaseResponse>>
    fun getPeriods(): Flow<Result<List<PeriodEntity>>>
    fun getTransactions(): Flow<Result<List<TransactionEntity>>>
    fun getPayments(): Flow<Result<List<PaymentEntity>>>
}
