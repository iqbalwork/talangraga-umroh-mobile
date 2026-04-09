package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
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
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class AddTransactionViewModel(
    private val repository: Repository,
    private val session: Session
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

            is AddTransactionEvent.SetSelectedPayment -> {
                _uiState.update { it.copy(selectedPayment = event.payment) }
            }

            is AddTransactionEvent.SetImageUri -> {
                _uiState.update { it.copy(imageUri = event.uri) }
            }

            is AddTransactionEvent.SubmitTransaction -> {
                addTransaction(event.amount, event.dateMillis, event.time, event.user)
            }

            is AddTransactionEvent.SetSelectedUser -> {
                _uiState.update { it.copy(selectedUser = event.user) }
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
                        _uiState.update { it.copy(payments = result.data) }
                    }
                }
            }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalTime::class)
    private fun addTransaction(amountString: String, dateMillis: Long?, time: String, user: UserUIData?) {
        val amount = amountString.replace(Regex("[^0-9]"), "").toDoubleOrNull() ?: 0.0
        var transactionDateIso = ""
        
        if (dateMillis != null && time.isNotEmpty()) {
            try {
                val instant = kotlin.time.Instant.fromEpochMilliseconds(dateMillis)
                val date = instant.toLocalDateTime(TimeZone.UTC).date
                val timeParts = time.split(":")
                val localDateTime = LocalDateTime(
                    year = date.year,
                    month = date.month,
                    day = date.day,
                    hour = timeParts[0].toInt(),
                    minute = timeParts[1].toInt(),
                    second = 0,
                    nanosecond = 0
                )
                transactionDateIso = localDateTime.toInstant(TimeZone.currentSystemDefault()).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        val transactionDate = transactionDateIso.ifEmpty { "$dateMillis $time" }
        val userId = user?.id ?: uiState.value.selectedUser?.id

        _uiState.update { it.copy(isLoading = true) }
        repository.addTransaction(
            userId = userId,
            reportedByUserId = session.userProfile.value?.id ?: 1,
            amount = amount,
            transactionDate = transactionDate,
            periodeId = uiState.value.selectedPeriod?.periodId ?: 1,
            paymentId = uiState.value.selectedPayment?.paymentId ?: 1,
            file = uiState.value.imageUri
        ).onEach { result ->
            _uiState.update { it.copy(isLoading = false) }
            when (result) {
                is Result.Success -> {
                    _effect.emit(AddTransactionEffect.ShowToastSuccess("Transaksi berhasil ditambahkan"))
                    _effect.emit(AddTransactionEffect.NavigateBack)
                }
                is Result.Error -> {
                    _effect.emit(AddTransactionEffect.ShowToastError(result.t.message ?: "Gagal menambahkan transaksi"))
                }
            }
        }.launchIn(viewModelScope)
    }
}
