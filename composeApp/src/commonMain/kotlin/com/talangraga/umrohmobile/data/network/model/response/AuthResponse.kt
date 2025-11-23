package com.talangraga.umrohmobile.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val jwt: String? = null,
    val user: UserResponse? = null
)
