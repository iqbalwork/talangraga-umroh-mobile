package com.talangraga.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Formats(
    @SerialName("thumbnail")
    val thumbnail: com.talangraga.data.network.model.response.ImageFormat? = null,
    @SerialName("small")
    val small: com.talangraga.data.network.model.response.ImageFormat? = null,
    @SerialName("medium")
    val medium: com.talangraga.data.network.model.response.ImageFormat? = null,
    @SerialName("large")
    val large: com.talangraga.data.network.model.response.ImageFormat? = null
)
