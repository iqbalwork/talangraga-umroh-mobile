package com.talangraga.umrohmobile.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("documentId")
    val documentId: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("width")
    val width: Int? = null,
    @SerialName("height")
    val height: Int? = null,
    @SerialName("formats")
    val formats: Formats? = null,
    @SerialName("hash")
    val hash: String? = null,
    @SerialName("ext")
    val ext: String? = null,
    @SerialName("mime")
    val mime: String? = null,
    @SerialName("size")
    val size: Double? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("previewUrl")
    val previewUrl: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("publishedAt")
    val publishedAt: String? = null
)