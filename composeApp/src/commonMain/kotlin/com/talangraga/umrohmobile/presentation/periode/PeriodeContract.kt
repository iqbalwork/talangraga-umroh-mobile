package com.talangraga.umrohmobile.presentation.periode

import com.talangraga.data.local.database.model.PeriodEntity

data class PeriodeState(
    val isLoading: Boolean = false,
    val periods: List<PeriodEntity> = emptyList(),
    val errorMessage: String? = null
)

sealed interface PeriodeEvent {
    data object GetPeriods : PeriodeEvent
    data object ClearError : PeriodeEvent
    data class AddPeriode(
        val periodeName: String,
        val startDate: String,
        val endDate: String
    ) : PeriodeEvent
}

sealed interface PeriodeEffect {
    data class ShowToastError(val message: String) : PeriodeEffect
    data class ShowToastSuccess(val message: String) : PeriodeEffect
}
