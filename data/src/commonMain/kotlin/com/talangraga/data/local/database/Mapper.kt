package com.talangraga.data.local.database

import com.talangraga.PaymentData
import com.talangraga.PeriodData
import com.talangraga.TransactionData
import com.talangraga.UserData
import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.data.local.database.model.TransactionEntity
import com.talangraga.data.local.database.model.UserEntity

fun PaymentData.toPaymentEntity(): PaymentEntity {
    return PaymentEntity(
        paymentId = paymentId.toInt(),
        paymentName = paymentName,
        paymentType = paymentType
    )
}

fun PeriodData.toPeriodEntity(): PeriodEntity {
    return PeriodEntity(
        periodId = periodId.toInt(),
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
