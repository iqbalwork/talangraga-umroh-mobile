package com.talangraga.umrohmobile.data.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val identifier: String,
    val password: String
)