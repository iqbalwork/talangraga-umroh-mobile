package com.talangraga.umrohmobile.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("jwt")
    val jwt: String,
    @SerialName("user")
    val user: UserResponse
): ErrorResponse()