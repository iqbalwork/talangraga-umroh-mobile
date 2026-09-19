package com.talangraga.data.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("fullname")
    val fullname: String,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("phone_number")
    val phone: String? = null,
    @SerialName("domisili")
    val domicile: String? = null,
    @SerialName("user_type")
    val userType: String,
    @SerialName("image_profile")
    val imageProfile: String? = null
)
