package com.talangraga.umrohmobile.domain.repository

import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.data.network.model.response.AuthResponse
import com.talangraga.umrohmobile.data.network.model.response.BaseResponse
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(identifier: String, password: String): Flow<ApiResponse<AuthResponse, BaseResponse>>
    fun getLoginProfile(): Flow<ApiResponse<UserResponse, BaseResponse>>
}
