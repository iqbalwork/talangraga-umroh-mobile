package com.talangraga.umrohmobile.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "periode_data")
data class PeriodEntity(
    @PrimaryKey
    @ColumnInfo(name = "period_id")
    val periodId: Int,
    @ColumnInfo(name = "document_id")
    val documentId: String,
    @ColumnInfo(name = "periode_name")
    val periodeName: String,
    @ColumnInfo(name = "start_date")
    val startDate: String,
    @ColumnInfo(name = "end_date")
    val endDate: String
)
