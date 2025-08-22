package com.talangraga.talangragaumrohmobile.domain.repository

import com.talangraga.talangragaumrohmobile.data.network.ApiResponse
import com.talangraga.talangragaumrohmobile.data.network.model.response.AuthResponse
import com.talangraga.talangragaumrohmobile.data.network.model.response.ErrorResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(identifier: String, password: String): Flow<ApiResponse<AuthResponse, ErrorResponse>>
}
