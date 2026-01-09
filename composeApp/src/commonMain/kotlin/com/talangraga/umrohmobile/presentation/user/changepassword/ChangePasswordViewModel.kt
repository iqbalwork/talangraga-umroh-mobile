package com.talangraga.umrohmobile.presentation.user.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.network.api.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
class ChangePasswordViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _currentPassword = MutableStateFlow("")
    val currentPassword = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isPasswordChanged = MutableStateFlow<Boolean?>(null)
    val isPasswordChanged = _isPasswordChanged.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun onCurrentPasswordChange(password: String) {
        _currentPassword.value = password
    }

    fun onNewPasswordChange(password: String) {
        _newPassword.value = password
    }

    fun onConfirmPasswordChange(password: String) {
        _confirmPassword.value = password
    }

    fun changePassword() {
        _isLoading.update { true }
        repository.changePassword(
            currentPassword = currentPassword.value,
            newPassword = newPassword.value,
            confirmNewPassword = confirmPassword.value
        ).onEach { result ->
            when (result) {
                is Result.Error -> {
                    _isLoading.update { false }
                    _errorMessage.update { result.t.message }
                }

                is Result.Success -> {
                    _isLoading.update { false }
                    _isPasswordChanged.update { true }
                }
            }
        }.launchIn(viewModelScope)
    }

}
