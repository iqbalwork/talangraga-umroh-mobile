package com.talangraga.talangragaumrohmobile.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val jwt: String,
    val user: UserResponse
): ErrorResponse()