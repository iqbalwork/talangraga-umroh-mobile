package com.talangraga.umrohmobile.presentation.home

import SessionStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.data.local.session.TokenManager
import com.talangraga.umrohmobile.data.local.session.clearAll
import com.talangraga.umrohmobile.data.local.session.getUserProfile
import com.talangraga.umrohmobile.data.mapper.toUserEntity
import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.data.network.api.Result
import com.talangraga.umrohmobile.domain.repository.Repository
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
    private val repository: Repository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _profile = MutableStateFlow<UserEntity?>(null)
    val profile = _profile.asStateFlow()

    private val _periods = MutableStateFlow<List<PeriodEntity>>(emptyList())
    val periods = _periods.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions = _transactions.asStateFlow()

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
        getTransactions()
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
                    _profile.update { profile.toUserEntity() }
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
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun getTransactions() {
        _uiState.update { it.copy(transactions = SectionState.Loading) }
        repository.getTransactions()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(transactions = SectionState.Error(result.t.message.orEmpty())) }
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(transactions = SectionState.Success(result.data)) }
                        _transactions.update { result.data }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun clearSession() {
        viewModelScope.launch {
            sessionStore.clearAll()
            tokenManager.clearToken()
            _profile.update { null }
            _isLoading.update { false }
            _errorMessage.update { null }
        }
    }

}