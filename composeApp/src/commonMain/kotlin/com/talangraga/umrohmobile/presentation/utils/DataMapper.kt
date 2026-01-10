package com.talangraga.umrohmobile.presentation.utils

import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.data.local.database.model.TransactionEntity
import com.talangraga.data.local.database.model.UserEntity
import com.talangraga.data.network.model.response.UserResponse
import com.talangraga.umrohmobile.presentation.transaction.model.PaymentUIData
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

fun UserResponse.toUiData(): UserUIData {
    return UserUIData(
        id = id,
        username = username.orEmpty(),
        fullname = fullname.orEmpty(),
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        domicile = domisili.orEmpty(),
        userType = userType.orEmpty(),
        imageProfileUrl = imageProfile.orEmpty(),
        isActive = isActive ?: false,
    )
}

fun UserEntity.toUiData(): UserUIData {
    return UserUIData(
        id = userId,
        username = userName,
        fullname = fullname,
        email = email,
        phone = phone,
        domicile = domisili,
        userType = userType,
        imageProfileUrl = imageProfileUrl,
        isActive = true,
    )
}

fun TransactionEntity.toUIData(): TransactionUiData {
    return TransactionUiData(
        transactionId = transactionId,
        amount = amount,
        transactionDate = this.transactionDate,
        statusTransaksi = this.statusTransaksi,
        reportedDate = this.reportedDate,
        buktiTransferUrl = this.buktiTransferUrl,
        reportedBy = this.reportedBy,
        confirmedBy = this.confirmedBy,
        paymentType = this.paymentType,
        paymentName = this.paymentName
    )
}

fun PaymentEntity.toUIData(): PaymentUIData {
    return PaymentUIData(
        id = paymentId,
        paymentName = paymentName,
        paymentType = paymentType
    )
}
