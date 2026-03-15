package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class AddTransactionState(
    val users: List<UserUIData> = emptyList(),
    val periods: List<PeriodEntity> = emptyList(),
    val errorMessage: String? = null,
    val selectedPeriod: PeriodEntity? = null
)

sealed interface AddTransactionEvent {
    data object GetListUser : AddTransactionEvent
    data object GetPeriods : AddTransactionEvent
    data object GetListPayments : AddTransactionEvent
    data class SetSelectedPeriod(val period: PeriodEntity?) : AddTransactionEvent
}

sealed interface AddTransactionEffect {
    data class ShowToastError(val message: String) : AddTransactionEffect
}
