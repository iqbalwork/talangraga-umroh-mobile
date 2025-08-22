package com.talangraga.talangragaumrohmobile.presentation.login

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    object Success : LoginState
    data class Error(val errorMessage: String) : LoginState
}
