package com.talangraga.umrohmobile.presentation.transaction.detailtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.utils.toUIData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class TransactionDetailViewModel(
    private val repository: Repository,
    private val session: Session
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionDetailState())
    val uiState: StateFlow<TransactionDetailState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TransactionDetailEffect>()
    val effect: SharedFlow<TransactionDetailEffect> = _effect.asSharedFlow()

    init {
        val userType = session.userProfile.value?.userType?.lowercase()
        _uiState.update { it.copy(isAdmin = userType == "admin") }
    }

    fun onEvent(event: TransactionDetailEvent) {
        when (event) {
            is TransactionDetailEvent.SetInitialData -> {
                _uiState.update { it.copy(transaction = event.transaction) }
            }
            TransactionDetailEvent.ConfirmTransaction -> confirmTransaction()
            TransactionDetailEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun confirmTransaction() {
        val transactionId = _uiState.value.transaction?.transactionId ?: return
        _uiState.update { it.copy(isLoading = true) }
        repository.updateTransactionStatus(transactionId, "completed")
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.t.message
                            )
                        }
                    }
                    is Result.Success -> {
                        val prevPeriodName = _uiState.value.transaction?.periodName.orEmpty()
                        val prevPeriodStart = _uiState.value.transaction?.periodStartDate.orEmpty()
                        val prevPeriodEnd = _uiState.value.transaction?.periodEndDate.orEmpty()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                transaction = result.data.toUIData().copy(
                                    periodName = prevPeriodName,
                                    periodStartDate = prevPeriodStart,
                                    periodEndDate = prevPeriodEnd
                                ),
                                isUpdateSuccess = true
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }
}
