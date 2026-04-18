package com.talangraga.umrohmobile.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
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
    private val repository: Repository,
    private val session: Session
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionState())
    val uiState: StateFlow<TransactionState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TransactionEffect>()
    val effect: SharedFlow<TransactionEffect> = _effect.asSharedFlow()

    init {
        val isMember = session.userProfile.value?.userType?.lowercase() != "admin"
        _uiState.update { it.copy(isMember = isMember) }

        onEvent(TransactionEvent.GetPeriods)
        onEvent(TransactionEvent.GetUsers)
        onEvent(TransactionEvent.GetTransactions(null))
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
                        val mappedUsers = result.data.map { user -> user.toUiData() }
                        _uiState.update { state -> 
                            var updatedUser = state.selectedUser
                            if (state.isMember) {
                                val currentUserId = session.userProfile.value?.id
                                updatedUser = mappedUsers.find { it.id == currentUserId }
                                // get transactions immediately for this user if it's newly set
                                if (updatedUser != null && state.selectedUser?.id != updatedUser.id) {
                                    getTransactions(periodId = state.selectedPeriod?.periodId, userId = updatedUser.id)
                                }
                            }
                            state.copy(users = mappedUsers, selectedUser = updatedUser) 
                        }
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
                        _uiState.update { state ->
                            state.copy(
                                transactions = SectionState.Success(filteredData),
                                isLoading = false,
                                selectedPeriod = if (periodId == null) null else state.selectedPeriod
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }
}
