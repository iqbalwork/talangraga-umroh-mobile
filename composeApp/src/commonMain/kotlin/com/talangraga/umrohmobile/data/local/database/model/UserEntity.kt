package com.talangraga.umrohmobile.data.local.database.model

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
