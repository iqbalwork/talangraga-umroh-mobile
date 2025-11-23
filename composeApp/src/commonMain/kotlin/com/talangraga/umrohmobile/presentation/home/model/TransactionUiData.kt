package com.talangraga.umrohmobile.presentation.home.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionUiData(
    val transactionId: Int,
    val amount: Int,
    val transactionDate: String,
    val statusTransaksi: String,
    val reportedDate: String,
    val buktiTransferUrl: String
)
