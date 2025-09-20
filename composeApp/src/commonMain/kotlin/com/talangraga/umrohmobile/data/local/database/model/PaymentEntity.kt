package com.talangraga.umrohmobile.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_data")
data class PaymentEntity(
    @PrimaryKey
    @ColumnInfo(name = "payment_id")
    val paymentId: Int,
    @ColumnInfo(name = "document_id")
    val documentId: String,
    @ColumnInfo(name = "payment_name")
    val paymentName: String,
    @ColumnInfo(name = "payment_type")
    val paymentType: String
)
