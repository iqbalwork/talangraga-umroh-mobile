package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.ImageCompressor
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
import kotlinx.coroutines.launch
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

    private val imageCompressor = ImageCompressor()

    init {
        val isMemberUser = session.userProfile.value?.userType?.lowercase() != "admin"
        _uiState.update { it.copy(isMemberUser = isMemberUser) }

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
                if (event.uri != null) {
                    _uiState.update { it.copy(isLoading = true) }
                    viewModelScope.launch {
                        try {
                            val compressedFile = imageCompressor.compress(event.uri, 200 * 1024L)
                            _uiState.update { it.copy(imageUri = compressedFile, isLoading = false) }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            _uiState.update { it.copy(imageUri = event.uri, isLoading = false) }
                        }
                    }
                } else {
                    _uiState.update { it.copy(imageUri = null) }
                }
            }

            is AddTransactionEvent.SubmitTransaction -> {
                if (_uiState.value.isCollective) {
                    addCollectiveTransactions(event.dateMillis, event.time)
                } else {
                    addTransaction(event.amount, event.dateMillis, event.time, event.user)
                }
            }

            is AddTransactionEvent.SetSelectedUser -> {
                _uiState.update { it.copy(selectedUser = event.user) }
            }

            is AddTransactionEvent.SetIsCollective -> {
                _uiState.update { it.copy(isCollective = event.isCollective) }
            }

            is AddTransactionEvent.AddCollectiveMember -> {
                val currentMembers = _uiState.value.collectiveMembers.toMutableList()
                currentMembers.add(CollectiveMember(event.user, event.amount))
                _uiState.update { it.copy(collectiveMembers = currentMembers) }
            }

            is AddTransactionEvent.RemoveCollectiveMember -> {
                val currentMembers = _uiState.value.collectiveMembers.toMutableList()
                currentMembers.removeAll { it.user.id == event.user.id }
                _uiState.update { it.copy(collectiveMembers = currentMembers) }
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
                    _uiState.update { state -> 
                        var updatedSelectedUser = state.selectedUser
                        if (state.isMemberUser) {
                            val currentUserId = session.userProfile.value?.id
                            updatedSelectedUser = data.find { it.id == currentUserId }
                        }
                        state.copy(users = data, selectedUser = updatedSelectedUser) 
                    }
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
    private fun addCollectiveTransactions(dateMillis: Long?, time: String) {
        val state = _uiState.value
        val members = state.collectiveMembers
        if (members.isEmpty()) {
            viewModelScope.launch {
                _effect.emit(AddTransactionEffect.ShowToastError("Belum ada anggota yang ditambahkan"))
            }
            return
        }

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

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            var allSuccess = true
            val reportedByUserId = session.userProfile.value?.id ?: 1
            val periodeId = state.selectedPeriod?.periodId ?: 1
            val paymentId = state.selectedPayment?.paymentId ?: 1
            val imageFile = state.imageUri

            members.forEach { member ->
                val amount = member.amount.replace(Regex("[^0-9]"), "").toDoubleOrNull() ?: 0.0
                repository.addTransaction(
                    userId = member.user.id,
                    reportedByUserId = reportedByUserId,
                    amount = amount,
                    transactionDate = transactionDate,
                    periodeId = periodeId,
                    paymentId = paymentId,
                    file = imageFile
                ).collect { result ->
                    if (result is Result.Error) {
                        allSuccess = false
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
            if (allSuccess) {
                _effect.emit(AddTransactionEffect.ShowToastSuccess("Semua transaksi kolektif berhasil ditambahkan"))
                _effect.emit(AddTransactionEffect.NavigateBack)
            } else {
                _effect.emit(AddTransactionEffect.ShowToastError("Beberapa transaksi gagal ditambahkan"))
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun addTransaction(amountString: String, dateMillis: Long?, time: String, user: UserUIData?) {
        Napier.i { "TEST => Add Transaction Triggered" }
        val amount = amountString.replace(Regex("[^0-9]"), "").toDoubleOrNull() ?: 0.0
        var transactionDateIso = ""
        
        if (dateMillis != null && time.isNotEmpty()) {
            try {
                val instant = kotlin.time.Instant.fromEpochMilliseconds(dateMillis)
                // Use UTC to get the date from DatePicker millis (standard for Material3 DatePicker)
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
                // Convert the local time to an Instant using the system's timezone
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
                    Napier.i { "TEST => Add Transaction Succeed" }
                    _effect.emit(AddTransactionEffect.ShowToastSuccess("Transaksi berhasil ditambahkan"))
                    _effect.emit(AddTransactionEffect.NavigateBack)
                }
                is Result.Error -> {
                    Napier.i { "TEST => Add Transaction Error" }
                    _effect.emit(AddTransactionEffect.ShowToastError(result.t.message ?: "Gagal menambahkan transaksi"))
                }
            }
        }.launchIn(viewModelScope)
    }
}
