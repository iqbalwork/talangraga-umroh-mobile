package com.talangraga.umrohmobile.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeriodeResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("documentId")
    val documentId: String?,
    @SerialName("periodeName")
    val periodeName: String?,
    @SerialName("startDate")
    val startDate: String?,
    @SerialName("endDate")
    val endDate: String?
)