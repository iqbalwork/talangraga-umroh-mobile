package com.talangraga.data.network.model.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    @SerialName("current_password")
    val currentPassword: String?,
    @SerialName("new_password")
    val newPassword: String?,
    @SerialName("confirm_new_password")
    val confirmNewPassword: String?
)
