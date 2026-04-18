package com.talangraga.umrohmobile.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.utils.toUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class TransactionViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionState())
    val uiState: StateFlow<TransactionState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TransactionEffect>()
    val effect: SharedFlow<TransactionEffect> = _effect.asSharedFlow()

    init {
        onEvent(TransactionEvent.GetPeriods)
        onEvent(TransactionEvent.GetUsers)
        onEvent(TransactionEvent.GetTransactions())
    }

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.GetPeriods -> getPeriods()
            is TransactionEvent.GetUsers -> getUsers()
            is TransactionEvent.GetTransactions -> getTransactions(event.periodId, event.userId)
            is TransactionEvent.SelectPeriod -> {
                _uiState.update { it.copy(selectedPeriod = event.period) }
                getTransactions(periodId = event.period?.periodId, userId = _uiState.value.selectedUser?.id)
            }
            is TransactionEvent.SelectUser -> {
                _uiState.update { it.copy(selectedUser = event.user) }
                getTransactions(periodId = _uiState.value.selectedPeriod?.periodId, userId = event.user?.id)
            }
            is TransactionEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun getPeriods() {
        _uiState.update { it.copy(periods = SectionState.Loading) }
        repository.getPeriods()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(periods = SectionState.Error(result.t.message)) }
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(periods = SectionState.Success(result.data)) }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getUsers() {
        repository.getListUsers()
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { it.copy(users = result.data.map { user -> user.toUiData() }) }
                    }
                    else -> {}
                }
            }.launchIn(viewModelScope)
    }

    private fun getTransactions(periodId: Int? = null, userId: Int? = null) {
        _uiState.update { it.copy(transactions = SectionState.Loading, isLoading = true) }
        repository.getTransactions(periodId = periodId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(transactions = SectionState.Error(result.t.message), isLoading = false) }
                    }
                    is Result.Success -> {
                        val allData = result.data.map { it.toUIData() }
                        val filteredData = if (userId != null) {
                            allData.filter { it.userId == userId }
                        } else {
                            allData
                        }
                        _uiState.update { it.copy(transactions = SectionState.Success(filteredData), isLoading = false) }
                    }
                }
            }.launchIn(viewModelScope)
    }
}
