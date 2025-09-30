package com.talangraga.umrohmobile.presentation.home

import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity

data class HomeUiState(
    val profile: SectionState<UserEntity>,
    val periods: SectionState<List<PeriodEntity>>,
    val transactions: SectionState<List<TransactionEntity>>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class SectionState<out T> {
    object Loading : SectionState<Nothing>()
    data class Success<T>(val data: T) : SectionState<T>()
    data class Error(val message: String?) : SectionState<Nothing>()
}
