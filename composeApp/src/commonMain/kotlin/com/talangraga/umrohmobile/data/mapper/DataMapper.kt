package com.talangraga.umrohmobile.data.mapper

import com.talangraga.umrohmobile.BuildKonfig
import com.talangraga.umrohmobile.data.local.database.model.PaymentEntity
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.data.network.model.response.PaymentResponse
import com.talangraga.umrohmobile.data.network.model.response.PeriodeResponse
import com.talangraga.umrohmobile.data.network.model.response.TransactionResponse
import com.talangraga.umrohmobile.data.network.model.response.UserResponse

fun UserResponse.toUserEntity(): UserEntity {
    return UserEntity(
        userId = this.id ?: 0,
        userName = this.username.orEmpty(),
        fullname = this.fullname.orEmpty(),
        email = this.email.orEmpty(),
        phone = this.phone.orEmpty(),
        domisili = this.domisili.orEmpty(),
        userType = this.userType.orEmpty(),
        imageProfileUrl = "${BuildKonfig.BASE_URL.replace("/api/", "")}${this.imageProfile?.url}"
//        imageProfileUrl = if (BuildKonfig.BUILD_TYPE == "debug") {
//            "${
//                BuildConfig.STAGING_BASE_URL.replace(
//                    "/api/",
//                    ""
//                )
//            }${this.imageProfile?.url}"
//        } else {
//            "${
//                BuildConfig.PRODUCTION_BASE_URL.replace(
//                    "/api/",
//                    ""
//                )
//            }${this.imageProfile?.url}"
//        }
    )
}

fun PeriodeResponse.toPeriodEntity(): PeriodEntity {
    return PeriodEntity(
        periodId = this.id ?: 0,
        documentId = this.documentId.toString(),
        periodeName = this.periodeName.orEmpty(),
        startDate = this.startDate.orEmpty(),
        endDate = this.endDate.orEmpty()
    )
}

fun TransactionResponse.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = id ?: 0,
        amount = (amount ?: "0").toInt(),
        reportedDate = reportedDate.orEmpty(),
        transactionDate = transactionDate.orEmpty(),
        statusTransaksi = statusTransaksi.orEmpty(),
        buktiTransferUrl = "${BuildKonfig.BASE_URL.replace("/api/", "")}${buktiTransfer?.url}",
        paymentType = payment?.paymentType.orEmpty(),
        paymentName = payment?.paymentName.orEmpty(),
        reportedBy = reportedByUser?.fullname.orEmpty(),
        confirmedBy = confirmedByUser?.fullname.orEmpty(),
    )
}

fun PaymentResponse.toPaymentEntity(): PaymentEntity {
    return PaymentEntity(
        paymentId = id ?: 0,
        documentId = documentId.orEmpty(),
        paymentName = paymentName.orEmpty(),
        paymentType = paymentType.orEmpty()
    )
}