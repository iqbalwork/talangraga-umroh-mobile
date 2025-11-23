package com.talangraga.umrohmobile.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("payment_name")
    val paymentName: String,
    @SerialName("payment_type")
    val paymentType: String
)
