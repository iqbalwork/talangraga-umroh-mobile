package com.talangraga.umrohmobile.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_data")
data class TransactionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo("amount")
    val amount: Int,
    @ColumnInfo("reported_date")
    val reportedDate: String,
    @ColumnInfo("transaction_date")
    val transactionDate: String,
    @ColumnInfo("status_transaksi")
    val statusTransaksi: String,
    @ColumnInfo("bukti_transfer")
    val buktiTransferUrl: String,
    @ColumnInfo("payment_type")
    val paymentType: String,
    @ColumnInfo("payment_name")
    val paymentName: String,
    @ColumnInfo("reportedBy")
    val reportedBy: String,
    @ColumnInfo("confirmedBy")
    val confirmedBy: String,
)
