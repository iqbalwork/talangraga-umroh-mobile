package com.talangraga.umrohmobile.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_data")
data class PaymentEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "documentId")
    val documentId: String,
    @ColumnInfo(name = "paymentName")
    val paymentName: String,
    @ColumnInfo(name = "paymentType")
    val paymentType: String
)
