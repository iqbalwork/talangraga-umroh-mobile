package com.talangraga.talangragaumrohmobile.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class StrapiError(
    val status: Int? = 0,
    val name: String? = "",
    val message: String? = ""
)

@Serializable
open class ErrorResponse(
    var error: StrapiError? = null
)