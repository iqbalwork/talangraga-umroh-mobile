package com.talangraga.umrohmobile.presentation.home

import SessionStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.data.local.session.TokenManager
import com.talangraga.umrohmobile.data.local.session.clearAll
import com.talangraga.umrohmobile.data.local.session.getUserProfile
import com.talangraga.umrohmobile.data.mapper.toUserEntity
import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.data.network.api.Result
import com.talangraga.umrohmobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sessionStore: SessionStore,
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _profile = MutableStateFlow<UserEntity?>(null)
    val profile = _profile.asStateFlow()

    private val _periods = MutableStateFlow<List<PeriodEntity>>(emptyList())
    val periods = _periods.asStateFlow()

    init {
        getPeriods()
    }

    fun getProfile() {
        authRepository.getLoginProfile()
            .onEach { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _errorMessage.update { response.error.error?.message }
                    }

                    is ApiResponse.Success -> {
                        _isLoading.update { false }
                        _profile.update { it }
                        getLocalProfile()
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun getLocalProfile() {
        sessionStore.getUserProfile()
            .onEach { profile ->
                if (profile?.username.isNullOrBlank()) {
                    getProfile()
                } else {
                    _profile.update { profile.toUserEntity() }
                }
            }.launchIn(viewModelScope)
    }

    fun getPeriods() {
        authRepository.getPeriods()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _periods.update { result.data }
                    }
                }
            }.launchIn(viewModelScope)
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