package com.talangraga.umrohmobile.presentation.transaction.detailtransaction

import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData

data class TransactionDetailState(
    val transaction: TransactionUiData? = null,
    val isAdmin: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUpdateSuccess: Boolean = false
)

sealed interface TransactionDetailEvent {
    data class SetInitialData(val transaction: TransactionUiData) : TransactionDetailEvent
    data object ConfirmTransaction : TransactionDetailEvent
    data object ClearError : TransactionDetailEvent
}

sealed interface TransactionDetailEffect {
    data class ShowToastError(val message: String) : TransactionDetailEffect
    data object NavigateBack : TransactionDetailEffect
}
