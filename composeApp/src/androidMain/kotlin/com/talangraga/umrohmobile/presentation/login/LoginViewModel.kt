package com.talangraga.umrohmobile.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.domain.Result
import com.talangraga.umrohmobile.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    private val authRepository: AuthRepository by inject()

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnLoginClick -> {
                login(event.identifier, event.password)
            }
        }
    }

    private fun login(identifier: String, password: String) {
        authRepository.loginUser(identifier, password).onEach { result ->
            when (result) {
                is Result.Loading -> {
                    _uiState.value = LoginUiState.Loading
                }
                is Result.Success -> {
                    // Assuming result.data contains AuthResponse and it was successful
                    _uiState.value = LoginUiState.Success // Or Idle if success is only for effect
                    _effect.emit(LoginEffect.NavigateToHome)
                }
                is Result.Error -> {
                    _uiState.value = LoginUiState.Error("An unknown error occurred")
                    _effect.emit(LoginEffect.ShowError("An unknown error occurred"))
                }
            }
        }.launchIn(viewModelScope)
    }
}