package com.talangraga.umrohmobile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val jwt: String,
    val user: UserResponse
): StrapiErrorResponse()