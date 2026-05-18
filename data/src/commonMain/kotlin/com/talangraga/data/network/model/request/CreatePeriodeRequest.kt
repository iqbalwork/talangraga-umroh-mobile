package com.talangraga.data.network.model.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePeriodeRequest(
    @SerialName("periode_name")
    var periodeName: String,
    @SerialName("start_date")
    var startDate: String,
    @SerialName("end_date")
    var endDate: String
)