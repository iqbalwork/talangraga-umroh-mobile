package com.talangraga.umrohmobile.presentation.periode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.data.network.api.Result
import com.talangraga.shared.isDateInRange
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class PeriodeViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PeriodeState())
    val uiState: StateFlow<PeriodeState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PeriodeEffect>()
    val effect: SharedFlow<PeriodeEffect> = _effect.asSharedFlow()

    private var getPeriodsJob: Job? = null

    init {
        onEvent(PeriodeEvent.GetPeriods)
    }

    fun onEvent(event: PeriodeEvent) {
        when (event) {
            PeriodeEvent.GetPeriods -> getPeriods()
            PeriodeEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
            is PeriodeEvent.AddPeriode -> addPeriode(
                event.periodeName,
                event.startDate,
                event.endDate
            )
        }
    }

    private fun addPeriode(periodeName: String, startDate: String, endDate: String) {
        _uiState.update { it.copy(isLoading = true) }
        repository.addPeriode(periodeName, startDate, endDate)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val errorMsg = result.t.message ?: "Unknown error"
                        _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                        sendEffect(PeriodeEffect.ShowToastError(errorMsg))
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        sendEffect(PeriodeEffect.ShowToastSuccess("Berhasil menambahkan periode baru"))
                        getPeriods()
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getPeriods() {
        getPeriodsJob?.cancel()
        _uiState.update { it.copy(isLoading = true) }
        getPeriodsJob = combine(
            repository.getPeriods(),
            repository.getTransactions()
        ) { periodsResult, transactionsResult ->
            when {
                periodsResult is Result.Error -> {
                    val errorMsg = periodsResult.t.message ?: "Unknown error"
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                    sendEffect(PeriodeEffect.ShowToastError(errorMsg))
                }

                periodsResult is Result.Success -> {
                    val transactions = (transactionsResult as? Result.Success)?.data ?: emptyList()
                    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

                    val periodUiModels = periodsResult.data
                        .sortedWith(
                            compareByDescending<PeriodEntity> { it.startDate }
                                .thenByDescending { it.periodId }
                        )
                        .map { period ->
                            val periodTransactions = transactions.filter { it.periodId == period.periodId }
                            val completedSum = periodTransactions
                                .filter { it.statusTransaksi.equals("completed", ignoreCase = true) }
                                .sumOf { it.amount.toLong() }
                            val isActive = today.isDateInRange(period.startDate, period.endDate)

                            PeriodUiModel(
                                period = period,
                                totalAmount = completedSum,
                                transactionCount = periodTransactions.size,
                                isActive = isActive
                            )
                        }

                    _uiState.update {
                        it.copy(
                            periods = periodUiModels,
                            isLoading = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendEffect(effect: PeriodeEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
