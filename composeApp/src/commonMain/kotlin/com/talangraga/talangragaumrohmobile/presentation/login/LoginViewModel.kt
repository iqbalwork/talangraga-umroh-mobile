package com.talangraga.talangragaumrohmobile.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.talangragaumrohmobile.data.network.ApiResponse
import com.talangraga.talangragaumrohmobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(identifier: String, password: String) {
        _loginState.value = LoginState.Loading
        authRepository.login(identifier, password)
            .onEach { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _loginState.value = LoginState.Error(response.error.error?.message ?: "Unknown error")
                    }
                    is ApiResponse.Success -> {
                        _loginState.value = LoginState.Success
                    }
                }
            }
            .launchIn(viewModelScope)
    }

}