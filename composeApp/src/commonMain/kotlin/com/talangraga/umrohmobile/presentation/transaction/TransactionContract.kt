package com.talangraga.umrohmobile.presentation.transaction

import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class TransactionState(
    val transactions: SectionState<List<TransactionUiData>> = SectionState.Loading,
    val periods: SectionState<List<PeriodEntity>> = SectionState.Loading,
    val users: List<UserUIData> = emptyList(),
    val selectedPeriod: PeriodEntity? = null,
    val selectedUser: UserUIData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface TransactionEvent {
    data object GetPeriods : TransactionEvent
    data object GetUsers : TransactionEvent
    data class GetTransactions(val periodId: Int? = null, val userId: Int? = null) : TransactionEvent
    data class SelectPeriod(val period: PeriodEntity?) : TransactionEvent
    data class SelectUser(val user: UserUIData?) : TransactionEvent
    data object ClearError : TransactionEvent
}

sealed interface TransactionEffect {
    data class ShowError(val message: String) : TransactionEffect
}
