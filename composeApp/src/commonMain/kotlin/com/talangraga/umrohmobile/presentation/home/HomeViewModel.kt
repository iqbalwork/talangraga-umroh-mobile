package com.talangraga.umrohmobile.presentation.home

import SessionStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.data.local.session.clearAll
import com.talangraga.umrohmobile.data.local.session.getUserProfile
import com.talangraga.umrohmobile.data.mapper.toUserEntity
import com.talangraga.umrohmobile.data.network.api.ApiResponse
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

    private val _profile = MutableStateFlow<UserEntity?>(null)
    val profile = _profile.asStateFlow()

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

    fun clearSession() {
        viewModelScope.launch {
            sessionStore.clearAll()
        }
    }

}