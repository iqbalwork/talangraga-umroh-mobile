package com.talangraga.umrohmobile.presentation.transaction.detailtransaction

import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData

data class TransactionDetailState(
    val transaction: TransactionUiData? = null,
    val isAdmin: Boolean = false,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val showDeleteConfirm: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isUpdateSuccess: Boolean = false,
    val isDeleteSuccess: Boolean = false
)

sealed interface TransactionDetailEvent {
    data class SetInitialData(val transaction: TransactionUiData) : TransactionDetailEvent
    data class UpdateStatus(val status: String) : TransactionDetailEvent
    data object ConfirmTransaction : TransactionDetailEvent
    data object DeleteTransaction : TransactionDetailEvent
    data class SetShowDeleteConfirm(val show: Boolean) : TransactionDetailEvent
    data object ClearError : TransactionDetailEvent
    data object ClearSuccess : TransactionDetailEvent
}

sealed interface TransactionDetailEffect {
    data class ShowToastSuccess(val message: String) : TransactionDetailEffect
    data class ShowToastError(val message: String) : TransactionDetailEffect
    data object NavigateBack : TransactionDetailEffect
}
