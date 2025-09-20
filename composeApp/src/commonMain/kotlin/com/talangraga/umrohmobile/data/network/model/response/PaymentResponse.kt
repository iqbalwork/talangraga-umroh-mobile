package com.talangraga.umrohmobile.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("documentId")
    val documentId: String?,
    @SerialName("paymentName")
    val paymentName: String?,
    @SerialName("paymentType")
    val paymentType: String?
)