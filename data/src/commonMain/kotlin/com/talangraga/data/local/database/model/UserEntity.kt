package com.talangraga.data.local.database.model

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val userId: Int,
    val userName: String,
    val fullname: String,
    val email: String,
    val phone: String,
    val domisili: String,
    val userType: String,
    val imageProfileUrl: String,
)
