package com.talangraga.data.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTransactionStatusRequest(
    @SerialName("status")
    val status: String
)
