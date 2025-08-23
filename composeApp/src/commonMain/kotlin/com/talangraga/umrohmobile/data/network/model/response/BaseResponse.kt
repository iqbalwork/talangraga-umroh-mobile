package com.talangraga.umrohmobile.data.network.model.response

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
