package com.talangraga.umrohmobile.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.TokenManager
import com.talangraga.data.network.api.Result
import com.talangraga.shared.currentDate
import com.talangraga.shared.isDateInRange
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.utils.toUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val session: Session,
    private val tokenManager: TokenManager,
    private val repository: Repository,
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _periods = MutableStateFlow<List<PeriodEntity>>(emptyList())
    val periods = _periods.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionUiData>>(emptyList())
    val transactions = _transactions.asStateFlow()

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType.asStateFlow()

    val selectedPeriod = mutableStateOf<PeriodEntity?>(null)

    private val _uiState = MutableStateFlow(
        HomeUiState(
            profile = SectionState.Loading,
            periods = SectionState.Loading,
            transactions = SectionState.Loading,
            isLoading = true
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getProfile()
        getPeriods()
    }

    fun setSelectedPeriod(period: PeriodEntity?) {
        selectedPeriod.value = period
    }

    fun setUserType(type: String) {
        _userType.update { type }
    }

    fun getProfile() {
        _uiState.update { it.copy(profile = SectionState.Loading) }
        repository.getLoginProfile()
            .onEach { response ->
                when (response) {
                    is Result.Error -> {
                        _uiState.update { it.copy(profile = SectionState.Error(response.t.message)) }
                        _errorMessage.update { it }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(profile = SectionState.Success(response.data.toUiData())) }
                        getLocalProfile()
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun getLocalProfile() {
        _uiState.update { it.copy(profile = SectionState.Loading) }
        viewModelScope.launch {
            val profile = session.getProfile()
            if (profile?.username.isNullOrBlank()) {
                getProfile()
            } else {
                _uiState.update { it.copy(profile = SectionState.Success(profile.toUiData())) }
                if (_userType.value.isNullOrBlank()) {
                    _userType.update { profile.userType }
                }
            }
        }
    }

    fun getPeriods() {
        _uiState.update { it.copy(periods = SectionState.Loading, isLoading = true) }
        repository.getPeriods()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                periods = SectionState.Success(result.data),
                                isLoading = false
                            )
                        }
                        _periods.update { result.data }

                        if (selectedPeriod.value == null) {
                            setInitialPeriodAndTransactions(result.data)
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun setInitialPeriodAndTransactions(periods: List<PeriodEntity>) {
        // This function will run only once when periods are first fetched
        val currentPeriod = periods.find { data ->
            currentDate.isDateInRange(data.startDate, data.endDate)
        }

        setSelectedPeriod(currentPeriod)
        getTransactions(currentPeriod?.periodId)
    }

    fun getTransactions(periodId: Int? = null) {
        _uiState.update { it.copy(transactions = SectionState.Loading) }
        repository.getTransactions(periodId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        val data = result.data.map { it.toUIData() }
                        _transactions.update { data }
                        _uiState.update { it.copy(transactions = SectionState.Success(data)) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun clearSession() {
        viewModelScope.launch {
            session.clear()
            tokenManager.clearToken()
            _errorMessage.update { null }
        }
    }

}
