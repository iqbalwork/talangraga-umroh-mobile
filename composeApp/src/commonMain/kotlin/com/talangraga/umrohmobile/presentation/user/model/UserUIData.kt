package com.talangraga.umrohmobile.presentation.user.model

import kotlinx.serialization.Serializable

@Serializable
data class UserUIData(
    val id: Int,
    val username: String,
    val fullname: String,
    val email: String,
    val phone: String,
    val domicile: String,
    val userType: String,
    val imageProfileUrl: String,
    val isActive: Boolean
)
