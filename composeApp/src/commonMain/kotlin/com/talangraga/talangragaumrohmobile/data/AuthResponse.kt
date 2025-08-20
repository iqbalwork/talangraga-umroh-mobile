package com.talangraga.talangragaumrohmobile.data

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val jwt: String,
    val user: UserResponse
): StrapiErrorResponse()