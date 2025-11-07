package com.talangraga.umrohmobile.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String? = null,
    @SerialName("fullname")
    val fullname: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("phone_number")
    val phone: String? = null,
    @SerialName("domisili")
    val domisili: String? = null,
    @SerialName("user_type")
    val userType: String? = null,
    @SerialName("image_profile_url")
    val imageProfile: String? = null,
    @SerialName("is_active")
    val isActive: Boolean? = null
)
