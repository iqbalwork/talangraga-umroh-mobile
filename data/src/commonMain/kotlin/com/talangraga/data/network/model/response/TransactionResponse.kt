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
    val statusTransaksi: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("user")
    val user: UserResponse? = null,
    @SerialName("reported_date")
    val reportedDate: String? = null,
    @SerialName("reported_by_id")
    val reportedById: String? = null,
    @SerialName("reported_by")
    val reportedByUser: UserResponse? = null,
    @SerialName("confirmed_by_id")
    val confirmedById: String? = null,
    @SerialName("confirmed_by")
    val confirmedByUser: UserResponse? = null,
    @SerialName("periode_id")
    val periodeId: Int? = null,
    @SerialName("payment_id")
    val paymentId: Int? = null,
    @SerialName("payment")
    val payment: PaymentResponse? = null,
    @SerialName("periode")
    val periode: PeriodeResponse? = null,
)

@Serializable
data class ImportRowErrorResponse(
    @SerialName("row")
    val row: Int,
    @SerialName("data")
    val data: String? = null,
    @SerialName("error")
    val error: String
)

@Serializable
data class TransactionImportResultResponse(
    @SerialName("total_rows")
    val totalRows: Int,
    @SerialName("success_count")
    val successCount: Int,
    @SerialName("failed_count")
    val failedCount: Int,
    @SerialName("errors")
    val errors: List<ImportRowErrorResponse> = emptyList()
)

