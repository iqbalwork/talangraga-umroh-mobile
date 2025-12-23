package com.talangraga.umrohmobile.presentation.home

import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class HomeUiState(
    val profile: SectionState<UserUIData>,
    val periods: SectionState<List<PeriodEntity>>,
    val transactions: SectionState<List<TransactionUiData>>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class SectionState<out T> {
    object Loading : SectionState<Nothing>()
    data class Success<T>(val data: T) : SectionState<T>()
    data class Error(val message: String?) : SectionState<Nothing>()
}
