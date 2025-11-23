package com.talangraga.data.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("identifier")
    val identifier: String,
    @SerialName("password")
    val password: String
)
