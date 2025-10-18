package com.talangraga.umrohmobile.data.local.database.model

data class TransactionEntity(
    val transactionId: Int,
    val amount: Int,
    val reportedDate: String,
    val transactionDate: String,
    val statusTransaksi: String,
    val buktiTransferUrl: String,
    val paymentType: String,
    val paymentName: String,
    val reportedBy: String,
    val confirmedBy: String,
)
