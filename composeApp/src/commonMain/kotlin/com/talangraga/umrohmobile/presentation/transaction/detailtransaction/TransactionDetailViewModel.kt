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
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    private val repository: Repository,
    private val session: Session
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionDetailState())
    val uiState: StateFlow<TransactionDetailState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TransactionDetailEffect>()
    val effect: SharedFlow<TransactionDetailEffect> = _effect.asSharedFlow()

    init {
        checkAdminStatus()
    }

    private fun checkAdminStatus() {
        val userType = session.userProfile.value?.userType?.lowercase()
        _uiState.update { it.copy(isAdmin = userType == "admin") }
    }

    fun onEvent(event: TransactionDetailEvent) {
        when (event) {
            is TransactionDetailEvent.SetInitialData -> {
                val userType = session.userProfile.value?.userType?.lowercase()
                _uiState.update {
                    it.copy(
                        transaction = event.transaction,
                        isAdmin = userType == "admin"
                    )
                }
            }
            is TransactionDetailEvent.UpdateStatus -> updateTransactionStatus(event.status)
            TransactionDetailEvent.ConfirmTransaction -> updateTransactionStatus("completed")
            TransactionDetailEvent.DeleteTransaction -> deleteTransaction()
            is TransactionDetailEvent.SetShowDeleteConfirm -> {
                _uiState.update { it.copy(showDeleteConfirm = event.show) }
            }
            TransactionDetailEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
            TransactionDetailEvent.ClearSuccess -> _uiState.update { it.copy(successMessage = null) }
        }
    }

    private fun updateTransactionStatus(status: String) {
        val transactionId = _uiState.value.transaction?.transactionId ?: return
        _uiState.update { it.copy(isLoading = true, isUpdating = true) }
        repository.updateTransactionStatus(transactionId, status)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val msg = result.t.message ?: "Gagal mengubah status tabungan."
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isUpdating = false,
                                errorMessage = msg
                            )
                        }
                        _effect.emit(TransactionDetailEffect.ShowToastError(msg))
                    }
                    is Result.Success -> {
                        val prevPeriodName = _uiState.value.transaction?.periodName.orEmpty()
                        val prevPeriodStart = _uiState.value.transaction?.periodStartDate.orEmpty()
                        val prevPeriodEnd = _uiState.value.transaction?.periodEndDate.orEmpty()
                        val statusLabel = when (status.lowercase()) {
                            "completed" -> "Disetujui"
                            "on_process" -> "Diproses"
                            else -> "Menunggu"
                        }
                        val successMsg = "Status tabungan berhasil diubah menjadi \"$statusLabel\""
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isUpdating = false,
                                transaction = result.data.toUIData().copy(
                                    periodName = prevPeriodName,
                                    periodStartDate = prevPeriodStart,
                                    periodEndDate = prevPeriodEnd
                                ),
                                isUpdateSuccess = true,
                                successMessage = successMsg
                            )
                        }
                        _effect.emit(TransactionDetailEffect.ShowToastSuccess(successMsg))
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun deleteTransaction() {
        val transactionId = _uiState.value.transaction?.transactionId ?: return
        _uiState.update { it.copy(isLoading = true, isUpdating = true, showDeleteConfirm = false) }
        repository.deleteTransaction(transactionId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val msg = result.t.message ?: "Gagal menghapus data tabungan."
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isUpdating = false,
                                errorMessage = msg
                            )
                        }
                        _effect.emit(TransactionDetailEffect.ShowToastError(msg))
                    }
                    is Result.Success -> {
                        val successMsg = "Data tabungan berhasil dihapus."
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isUpdating = false,
                                isDeleteSuccess = true,
                                successMessage = successMsg
                            )
                        }
                        _effect.emit(TransactionDetailEffect.ShowToastSuccess(successMsg))
                        _effect.emit(TransactionDetailEffect.NavigateBack)
                    }
                }
            }.launchIn(viewModelScope)
    }
}

