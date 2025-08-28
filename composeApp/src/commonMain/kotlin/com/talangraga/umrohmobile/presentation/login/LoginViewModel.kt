package com.talangraga.umrohmobile.presentation.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loginSucceed = MutableStateFlow<Boolean?>(null)
    val loginSucceed = _loginSucceed.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    var identifier = mutableStateOf("")
    var password = mutableStateOf("")

    fun onIdentifierChange(newUsername: String) {
        identifier.value = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun login() {
        _isLoading.update { true }
        authRepository.login(identifier.value, password.value)
            .onEach { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _isLoading.update { false }
                        _errorMessage.update { response.error.error?.message }
                    }

                    is ApiResponse.Success -> {
                        _loginSucceed.update { true }
                        _isLoading.update { false }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

}