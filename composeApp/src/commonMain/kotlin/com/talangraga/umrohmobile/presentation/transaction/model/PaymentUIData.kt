package com.talangraga.umrohmobile.presentation.transaction.model

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
data class PaymentUIData(
    val id: Int,
    val paymentName: String,
    val paymentType: String
)

data class PaymentGroupUIData(
    val paymentType: String,
    val paymentList: List<PaymentUIData>
)
