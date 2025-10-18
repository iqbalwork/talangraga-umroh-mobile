package com.talangraga.umrohmobile.data.local.database

import com.talangraga.umrohmobile.PaymentData
import com.talangraga.umrohmobile.PeriodData
import com.talangraga.umrohmobile.TransactionData
import com.talangraga.umrohmobile.UserData
import com.talangraga.umrohmobile.data.local.database.model.PaymentEntity
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity

fun PaymentData.toPaymentEntity(): PaymentEntity {
    return PaymentEntity(
        paymentId = paymentId.toInt(),
        documentId = documentId.orEmpty(),
        paymentName = paymentName,
        paymentType = paymentType
    )
}

fun PeriodData.toPeriodEntity(): PeriodEntity {
    return PeriodEntity(
        periodId = periodId.toInt(),
        documentId = documentId.orEmpty(),
        periodeName = periodeName,
        startDate = startDate,
        endDate = endDate
    )
}

fun TransactionData.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = transactionId.toInt(),
        amount = amount.toInt(),
        reportedDate = reportedDate,
        transactionDate = transactionDate,
        statusTransaksi = statusTransaksi,
        buktiTransferUrl = buktiTransferUrl,
        paymentType = paymentType.orEmpty(),
        paymentName = paymentName.orEmpty(),
        reportedBy = reportedBy.orEmpty(),
        confirmedBy = confirmedBy.orEmpty()
    )
}

fun UserData.toUserEntity(): UserEntity {
    return UserEntity(
        userId = userId.toInt(),
        userName = username,
        fullname = fullname,
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        domisili = domisili.orEmpty(),
        userType = userType.orEmpty(),
        imageProfileUrl = imageProfileUrl.orEmpty()
    )
}
