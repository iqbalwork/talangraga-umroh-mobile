package com.talangraga.umrohmobile.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int?, // Changed to nullable
    val username: String?, // Changed to nullable
    val email: String?, // Changed to nullable
    val provider: String? = null,
    val confirmed: Boolean?, // Changed to nullable
    val blocked: Boolean?, // Changed to nullable
    val fullname: String? = null,
    val phone: String? = null,
    val domisili: String? = null,
    val userType: String? = null
)