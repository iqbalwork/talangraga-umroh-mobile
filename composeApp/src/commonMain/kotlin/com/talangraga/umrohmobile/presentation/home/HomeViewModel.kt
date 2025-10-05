package com.talangraga.umrohmobile.presentation.home

import SessionStore
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.session.TokenManager
import com.talangraga.umrohmobile.data.local.session.clearAll
import com.talangraga.umrohmobile.data.local.session.getUserProfile
import com.talangraga.umrohmobile.data.mapper.toUserEntity
import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.data.network.api.Result
import com.talangraga.umrohmobile.domain.repository.Repository
import com.talangraga.umrohmobile.util.currentDate
import com.talangraga.umrohmobile.util.isDateInRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sessionStore: SessionStore,
    private val tokenManager: TokenManager,
    private val repository: Repository,
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _periods = MutableStateFlow<List<PeriodEntity>>(emptyList())
    val periods = _periods.asStateFlow()

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType.asStateFlow()

    private var isProfileInitialized = false
    val selectedPeriod = mutableStateOf<PeriodEntity?>(null)

    private val _uiState = MutableStateFlow(
        HomeUiState(
            profile = SectionState.Loading,
            periods = SectionState.Loading,
            transactions = SectionState.Loading
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getPeriods()
    }

    fun setSelectedPeriod(period: PeriodEntity?) {
        selectedPeriod.value = period
    }

    fun setUserType(type: String) {
        _userType.update { type }
    }

    fun getProfileIfNecessary(justLogin: Boolean) {
        if (justLogin) {
            getProfile()
        } else {
            getLocalProfile()
        }
        isProfileInitialized = true
    }

    fun getProfile() {
        _uiState.update { it.copy(profile = SectionState.Loading) }
        repository.getLoginProfile()
            .onEach { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _uiState.update { it.copy(profile = SectionState.Error(response.error.error?.message.orEmpty())) }
                    }

                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(profile = SectionState.Success(response.data.toUserEntity())) }
                        getLocalProfile()
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun getLocalProfile() {
        _uiState.update { it.copy(profile = SectionState.Loading) }
        sessionStore.getUserProfile()
            .onEach { profile ->
                if (profile?.username.isNullOrBlank()) {
                    getProfile()
                } else {
                    _uiState.update { it.copy(profile = SectionState.Success(profile.toUserEntity())) }
                    if (_userType.value.isNullOrBlank()) {
                        _userType.update { profile.userType.orEmpty() }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun getPeriods() {
        _uiState.update { it.copy(periods = SectionState.Loading) }
        repository.getPeriods()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(periods = SectionState.Error(result.t.message.orEmpty())) }
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(periods = SectionState.Success(result.data)) }
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
        } ?: periods.firstOrNull()

        setSelectedPeriod(currentPeriod)
        currentPeriod?.let {
            getTransactions(it.periodId)
        }
    }

    fun getTransactions(periodId: Int) {
        _uiState.update { it.copy(transactions = SectionState.Loading) }
        repository.getTransactions(periodId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(transactions = SectionState.Error(result.t.message.orEmpty())) }
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(transactions = SectionState.Success(result.data)) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun clearSession() {
        viewModelScope.launch {
            sessionStore.clearAll()
            tokenManager.clearToken()
            _errorMessage.update { null }
        }
    }

}