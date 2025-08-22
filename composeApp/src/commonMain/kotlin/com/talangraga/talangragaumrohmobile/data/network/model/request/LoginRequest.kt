package com.talangraga.talangragaumrohmobile.data.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val identifier: String,
    val password: String
)