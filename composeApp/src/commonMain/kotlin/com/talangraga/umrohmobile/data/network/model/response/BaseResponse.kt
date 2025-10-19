package com.talangraga.umrohmobile.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StrapiError(
    val status: Int? = 0,
    val name: String? = "",
    val message: String? = "",
)

@Serializable
open class BaseResponse(
    var error: StrapiError? = null
)

@Serializable
data class DataResponse<T>(
    @SerialName("data") val data: T?
): BaseResponse()