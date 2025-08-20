package com.talangraga.talangragaumrohmobile.data

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val username: String,
    val email: String,
    val provider: String? = null,
    val confirmed: Boolean,
    val blocked: Boolean,
    val fullname: String? = null,
    val phone: String? = null,
    val domisili: String? = null,
    val userType: String? = null
)