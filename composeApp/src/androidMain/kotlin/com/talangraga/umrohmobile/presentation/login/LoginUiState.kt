package com.talangraga.umrohmobile.presentation.login

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState() // Indicates login process was successful, effect will handle navigation
    data class Error(val message: String) : LoginUiState()
}