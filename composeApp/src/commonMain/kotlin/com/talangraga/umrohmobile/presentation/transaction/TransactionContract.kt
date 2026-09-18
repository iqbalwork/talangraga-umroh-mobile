package com.talangraga.umrohmobile.presentation.transaction

import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class ImportRowErrorUi(
    val row: Int,
    val data: String? = null,
    val error: String
)

data class ImportResultUiData(
    val totalRows: Int,
    val successCount: Int,
    val failedCount: Int,
    val errors: List<ImportRowErrorUi> = emptyList()
)

data class TransactionState(
    val transactions: SectionState<List<TransactionUiData>> = SectionState.Loading,
    val periods: SectionState<List<PeriodEntity>> = SectionState.Loading,
    val users: List<UserUIData> = emptyList(),
    val selectedPeriod: PeriodEntity? = null,
    val selectedUser: UserUIData? = null,
    val isLoading: Boolean = false,
    val isMember: Boolean = false,
    val isExporting: Boolean = false,
    val isImporting: Boolean = false,
    val importResult: ImportResultUiData? = null,
    val errorMessage: String? = null
)

sealed interface TransactionEvent {
    data object GetPeriods : TransactionEvent
    data object GetUsers : TransactionEvent
    data class GetTransactions(val periodId: Int? = null, val userId: Int? = null) : TransactionEvent
    data class SelectPeriod(val period: PeriodEntity?) : TransactionEvent
    data class SelectUser(val user: UserUIData?) : TransactionEvent
    data class ExportTransactions(val format: String) : TransactionEvent
    data class ImportTransactions(val fileBytes: ByteArray, val fileName: String) : TransactionEvent
    data object DismissImportResult : TransactionEvent
    data object ClearError : TransactionEvent
}

sealed interface TransactionEffect {
    data class ShowError(val message: String) : TransactionEffect
    data class ShowMessage(val message: String) : TransactionEffect
    data class ExportSuccess(val bytes: ByteArray, val fileName: String, val format: String) : TransactionEffect
}

