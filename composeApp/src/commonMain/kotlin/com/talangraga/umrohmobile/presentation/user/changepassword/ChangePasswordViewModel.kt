package com.talangraga.umrohmobile.presentation.user.changepassword

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

class ChangePasswordViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordState())
    val uiState: StateFlow<ChangePasswordState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ChangePasswordEffect>()
    val effect: SharedFlow<ChangePasswordEffect> = _effect.asSharedFlow()

    fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.OnCurrentPasswordChange -> _uiState.update { it.copy(currentPassword = event.password) }
            is ChangePasswordEvent.OnNewPasswordChange -> _uiState.update { it.copy(newPassword = event.password) }
            is ChangePasswordEvent.OnConfirmPasswordChange -> _uiState.update { it.copy(confirmPassword = event.password) }
            is ChangePasswordEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
            is ChangePasswordEvent.ChangePassword -> changePassword()
        }
    }

    private fun changePassword() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }
        repository.changePassword(
            currentPassword = state.currentPassword,
            newPassword = state.newPassword,
            confirmNewPassword = state.confirmPassword
        ).onEach { result ->
            when (result) {
                is Result.Error -> {
                    val message = result.t.message
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }

                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, isPasswordChanged = true) }
                }
            }
        }.launchIn(viewModelScope)
    }
}
