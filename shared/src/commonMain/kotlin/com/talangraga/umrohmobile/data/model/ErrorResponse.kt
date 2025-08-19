package com.talangraga.umrohmobile.data.model

// shared/src/commonMain/kotlin/com/yourapp/shared/model/StrapiErrorResponse.kt

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