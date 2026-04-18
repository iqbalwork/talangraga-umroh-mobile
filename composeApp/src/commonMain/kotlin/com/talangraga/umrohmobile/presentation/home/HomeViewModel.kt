package com.talangraga.umrohmobile.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.TokenManager
import com.talangraga.data.network.api.Result
import com.talangraga.shared.currentDate
import com.talangraga.shared.isDateInRange
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
import kotlinx.coroutines.launch

class HomeViewModel(
    val session: Session,
    private val tokenManager: TokenManager,
    private val repository: Repository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState(isLoading = true))
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        onEvent(HomeEvent.GetProfile)
        onEvent(HomeEvent.GetPeriods)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SetSelectedPeriod -> {
                _uiState.update { it.copy(selectedPeriod = event.period) }
            }
            is HomeEvent.SetUserType -> {
                _uiState.update { it.copy(userType = event.type) }
            }
            is HomeEvent.GetProfile -> {
                getProfile()
            }
            is HomeEvent.GetLocalProfile -> {
                getLocalProfile()
            }
            is HomeEvent.GetPeriods -> {
                getPeriods()
            }
            is HomeEvent.GetTransactions -> {
                getTransactions(event.periodId)
            }
            is HomeEvent.ClearSession -> {
                clearSession()
            }
            is HomeEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun getProfile() {
        _uiState.update { it.copy(profile = SectionState.Loading) }
        repository.getLoginProfile()
            .onEach { response ->
                when (response) {
                    is Result.Error -> {
                        val errorMsg = response.t.message
                        _uiState.update { it.copy(profile = SectionState.Error(errorMsg), errorMessage = errorMsg) }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(profile = SectionState.Success(response.data.toUiData())) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getLocalProfile() {
        _uiState.update { it.copy(profile = SectionState.Loading) }
        viewModelScope.launch {
            session.userProfile.value?.let { userResponse ->
                _uiState.update {
                    it.copy(
                        profile = SectionState.Success(userResponse.toUiData()),
                        userType = if (it.userType.isNullOrBlank()) userResponse.userType else it.userType
                    )
                }
            }
        }
    }

    private fun getPeriods() {
        _uiState.update { it.copy(periods = SectionState.Loading, isLoading = true) }
        repository.getPeriods()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val errorMsg = result.t.message
                        _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                    }

                    is Result.Success -> {
                        val data = result.data
                        _uiState.update {
                            it.copy(
                                periods = SectionState.Success(data),
                                isLoading = false
                            )
                        }

                        // Only set initial period if it's not already set
                        if (_uiState.value.selectedPeriod == null) {
                            val currentPeriod = data.find { data ->
                                currentDate.isDateInRange(data.startDate, data.endDate)
                            }
                            _uiState.update { it.copy(selectedPeriod = currentPeriod) }
                            onEvent(HomeEvent.GetTransactions(currentPeriod?.periodId))
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getTransactions(periodId: Int? = null) {
        _uiState.update { it.copy(transactions = SectionState.Loading) }
        repository.getTransactions(periodId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val errorMsg = result.t.message
                        _uiState.update { it.copy(errorMessage = errorMsg) }
                    }

                    is Result.Success -> {
                        val data = result.data.map { it.toUIData() }
                        _uiState.update { it.copy(transactions = SectionState.Success(data)) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun clearSession() {
        viewModelScope.launch {
            session.clear()
            tokenManager.clearToken()
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

}
