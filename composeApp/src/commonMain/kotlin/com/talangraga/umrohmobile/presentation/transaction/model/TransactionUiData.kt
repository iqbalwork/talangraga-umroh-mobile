package com.talangraga.umrohmobile.presentation.transaction.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionUiData(
    val transactionId: Int,
    val amount: Int,
    val transactionDate: String,
    val statusTransaksi: String,
    val reportedDate: String,
    val reportedBy: String,
    val confirmedBy: String,
    val userName: String,
    val buktiTransferUrl: String,
    val paymentType: String,
    val paymentName: String,
    val userId: Int,
    val periodId: Int
)
