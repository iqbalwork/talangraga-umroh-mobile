package com.talangraga.umrohmobile.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Formats(
    @SerialName("thumbnail")
    val thumbnail: ImageFormat? = null,
    @SerialName("small")
    val small: ImageFormat? = null,
    @SerialName("medium")
    val medium: ImageFormat? = null,
    @SerialName("large")
    val large: ImageFormat? = null
)