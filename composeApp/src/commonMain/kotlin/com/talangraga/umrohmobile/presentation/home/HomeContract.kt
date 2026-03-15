package com.talangraga.umrohmobile.presentation.home

import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

sealed class SectionState<out T> {
    data object Loading : SectionState<Nothing>()
    data class Success<T>(val data: T) : SectionState<T>()
    data class Error(val message: String?) : SectionState<Nothing>()
}

data class HomeState(
    val profile: SectionState<UserUIData> = SectionState.Loading,
    val periods: SectionState<List<PeriodEntity>> = SectionState.Loading,
    val transactions: SectionState<List<TransactionUiData>> = SectionState.Loading,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedPeriod: PeriodEntity? = null,
    val userType: String? = null
)

sealed interface HomeEvent {
    data class SetSelectedPeriod(val period: PeriodEntity?) : HomeEvent
    data class SetUserType(val type: String) : HomeEvent
    data object GetProfile : HomeEvent
    data object GetLocalProfile : HomeEvent
    data object GetPeriods : HomeEvent
    data class GetTransactions(val periodId: Int? = null) : HomeEvent
    data object ClearSession : HomeEvent
    data object ClearError : HomeEvent
}

sealed interface HomeEffect {
    data class ShowToastError(val message: String) : HomeEffect
}
