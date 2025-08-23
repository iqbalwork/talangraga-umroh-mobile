package com.talangraga.umrohmobile.presentation.home

import SessionStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.data.local.clearAll
import com.talangraga.umrohmobile.data.local.getUserProfile
import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import com.talangraga.umrohmobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sessionStore: SessionStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _profile = MutableStateFlow<UserResponse?>(null)
    val profile = _profile.asStateFlow()

    init {
        getProfile()
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
                        getUserProfile()
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun getUserProfile() {
        sessionStore.getUserProfile()
            .onEach { profile ->
                _profile.update { profile }
            }.launchIn(viewModelScope)
    }

    fun clearSession() {
        viewModelScope.launch {
            sessionStore.clearAll()
        }
    }

}