package com.talangraga.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val jwt: String? = null,
    val user: com.talangraga.data.network.model.response.UserResponse? = null
)
