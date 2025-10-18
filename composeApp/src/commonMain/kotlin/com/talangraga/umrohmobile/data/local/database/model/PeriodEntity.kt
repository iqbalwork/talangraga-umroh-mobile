package com.talangraga.umrohmobile.data.local.database.model

data class PeriodEntity(
    val periodId: Int,
    val documentId: String,
    val periodeName: String,
    val startDate: String,
    val endDate: String
)
