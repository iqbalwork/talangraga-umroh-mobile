package com.talangraga.talangragaumrohmobile.data

import kotlinx.serialization.Serializable

@Serializable
data class StrapiError(
    val status: Int,
    val name: String,
    val message: String
)

@Serializable
open class StrapiErrorResponse(
    var error: StrapiError? = null
)