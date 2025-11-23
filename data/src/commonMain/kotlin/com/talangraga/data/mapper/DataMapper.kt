package com.talangraga.data.mapper

import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.data.local.database.model.TransactionEntity
import com.talangraga.data.local.database.model.UserEntity
import com.talangraga.data.network.model.response.PaymentResponse
import com.talangraga.data.network.model.response.PeriodeResponse
import com.talangraga.data.network.model.response.TransactionResponse
import com.talangraga.data.network.model.response.UserResponse

fun com.talangraga.data.network.model.response.UserResponse.toUserEntity(): com.talangraga.data.local.database.model.UserEntity {
    return _root_ide_package_.com.talangraga.data.local.database.model.UserEntity(
        userId = this.id,
        userName = this.username.orEmpty(),
        fullname = this.fullname.orEmpty(),
        email = this.email.orEmpty(),
        phone = this.phone.orEmpty(),
        domisili = this.domisili.orEmpty(),
        userType = this.userType.orEmpty(),
        imageProfileUrl = this.imageProfile.orEmpty()
    )
}

fun com.talangraga.data.network.model.response.PeriodeResponse.toPeriodEntity(): com.talangraga.data.local.database.model.PeriodEntity {
    return _root_ide_package_.com.talangraga.data.local.database.model.PeriodEntity(
        periodId = this.id,
        periodeName = this.periodeName,
        startDate = this.startDate,
        endDate = this.endDate
    )
}

fun com.talangraga.data.network.model.response.TransactionResponse.toTransactionEntity(): com.talangraga.data.local.database.model.TransactionEntity {
    return _root_ide_package_.com.talangraga.data.local.database.model.TransactionEntity(
        transactionId = id,
        amount = amount.toInt(),
        reportedDate = reportedDate,
        transactionDate = transactionDate,
        statusTransaksi = statusTransaksi.orEmpty(),
        buktiTransferUrl = buktiTransfer.orEmpty(),
        paymentType = payment?.paymentType.orEmpty(),
        paymentName = payment?.paymentName.orEmpty(),
        reportedBy = reportedByUser?.fullname.orEmpty(),
        confirmedBy = confirmedByUser?.fullname.orEmpty(),
    )
}

fun com.talangraga.data.network.model.response.PaymentResponse.toPaymentEntity(): com.talangraga.data.local.database.model.PaymentEntity {
    return _root_ide_package_.com.talangraga.data.local.database.model.PaymentEntity(
        paymentId = id,
        paymentName = paymentName,
        paymentType = paymentType,
    )
}
