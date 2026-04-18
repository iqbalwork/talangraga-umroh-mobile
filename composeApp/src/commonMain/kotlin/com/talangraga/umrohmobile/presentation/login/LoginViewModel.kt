package com.talangraga.umrohmobile.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.network.api.Result
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

class LoginViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnIdentifierChange -> {
                _uiState.update { it.copy(identifier = event.identifier) }
            }

            is LoginEvent.OnPasswordChange -> {
                _uiState.update { it.copy(password = event.password) }
            }

            is LoginEvent.OnLoginClick -> {
                login()
            }

            is LoginEvent.ClearError -> {
                // Not needing a state change if we are firing as an effect, 
                // but handled just in case logic requires it later.
            }
        }
    }

    private fun login() {
        val currentState = _uiState.value
        _uiState.update { it.copy(isLoading = true) }
        repository.login(currentState.identifier, currentState.password)
            .onEach { response ->
                when (response) {
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        sendEffect(LoginEffect.ShowToastError(response.t.message ?: "Unknown error"))
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        sendEffect(LoginEffect.NavigateToMain)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
