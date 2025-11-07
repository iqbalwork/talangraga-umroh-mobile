package com.talangraga.umrohmobile.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataResponse<T>(
    @SerialName("data") val data: T?,
    @SerialName("code") val code: Int? = null,
    @SerialName("message") val message: String? = null,
)
