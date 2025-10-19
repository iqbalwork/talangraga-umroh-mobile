package com.talangraga.umrohmobile.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageFormat(
    @SerialName("name")
    val name: String? = null,
    @SerialName("hash")
    val hash: String? = null,
    @SerialName("ext")
    val ext: String? = null,
    @SerialName("mime")
    val mime: String? = null,
    @SerialName("path")
    val path: String? = null,
    @SerialName("width")
    val width: Int? = null,
    @SerialName("height")
    val height: Int? = null,
    @SerialName("size")
    val size: Double? = null,
    @SerialName("sizeInBytes")
    val sizeInBytes: Int? = null,
    @SerialName("url")
    val url: String? = null
)