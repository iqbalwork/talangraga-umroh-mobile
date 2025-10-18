package com.talangraga.umrohmobile.data.local.database.model

data class PaymentEntity(
    val paymentId: Int,
    val documentId: String,
    val paymentName: String,
    val paymentType: String
)
