package com.talangraga.umrohmobile.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("documentId")
    val documentId: String?,
    @SerialName("amount")
    val amount: String?,
    @SerialName("reportedDate")
    val reportedDate: String?,
    @SerialName("transactionDate")
    val transactionDate: String?,
    @SerialName("statusTransaksi")
    val statusTransaksi: String?,
    @SerialName("buktiTransferImage")
    val buktiTransfer: ImageResponse? = null,
    @SerialName("reportedByUser")
    val reportedByUser: UserResponse? = null,
    @SerialName("confirmedByUser")
    val confirmedByUser: UserResponse? = null,
    @SerialName("periode")
    val periode: PeriodeResponse? = null,
    @SerialName("payment")
    val payment: PaymentResponse? = null,
)