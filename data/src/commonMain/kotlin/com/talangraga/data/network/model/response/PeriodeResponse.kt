package com.talangraga.data.network.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeriodeResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("periode_name")
    val periodeName: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String
)
