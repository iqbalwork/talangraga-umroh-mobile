package com.talangraga.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("amount")
    val amount: Double,
    @SerialName("transaction_date")
    val transactionDate: String,
    @SerialName("bukti_transfer_url")
    val buktiTransfer: String? = null,
    @SerialName("status")
    val statusTransaksi: String?,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("user")
    val user: UserResponse? = null,
    @SerialName("reported_date")
    val reportedDate: String,
    @SerialName("reported_by")
    val reportedByUser: UserResponse? = null,
    @SerialName("confirmed_by")
    val confirmedByUser: UserResponse? = null,
    @SerialName("payment")
    val payment: PaymentResponse? = null,
    @SerialName("periode")
    val periode: PeriodeResponse? = null,
)
