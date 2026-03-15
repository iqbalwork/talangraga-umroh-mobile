package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.utils.toUiData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class AddTransactionViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionState())
    val uiState: StateFlow<AddTransactionState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AddTransactionEffect>()
    val effect: SharedFlow<AddTransactionEffect> = _effect.asSharedFlow()

    init {
        onEvent(AddTransactionEvent.GetListUser)
        onEvent(AddTransactionEvent.GetPeriods)
        onEvent(AddTransactionEvent.GetListPayments)
    }

    fun onEvent(event: AddTransactionEvent) {
        when (event) {
            is AddTransactionEvent.GetListUser -> getListUser()
            is AddTransactionEvent.GetPeriods -> getPeriods()
            is AddTransactionEvent.GetListPayments -> getListPayments()
            is AddTransactionEvent.SetSelectedPeriod -> {
                _uiState.update { it.copy(selectedPeriod = event.period) }
            }
        }
    }

    private fun getListUser() {
        repository.getLocalUsers().onEach { result ->
            when (result) {
                is Result.Error -> {
                    val message = result.t.message
                    _uiState.update { it.copy(errorMessage = message) }
                }

                is Result.Success -> {
                    val data = result.data.map { it.toUiData() }
                    _uiState.update { it.copy(users = data) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPeriods() {
        repository.getPeriods()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val message = result.t.message
                        _uiState.update { it.copy(errorMessage = message) }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(periods = result.data) }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getListPayments() {
        repository.getPayments()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val message = result.t.message
                        _uiState.update { it.copy(errorMessage = message) }
                    }

                    is Result.Success -> {
                        val data = result.data
                        Napier.i { data.toString() }
                    }
                }
            }.launchIn(viewModelScope)
    }
}
