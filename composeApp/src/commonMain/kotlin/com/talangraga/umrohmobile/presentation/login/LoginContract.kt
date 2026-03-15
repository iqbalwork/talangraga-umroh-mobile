package com.talangraga.umrohmobile.presentation.login

sealed interface LoginEvent {
    data class OnIdentifierChange(val identifier: String) : LoginEvent
    data class OnPasswordChange(val password: String) : LoginEvent
    data object OnLoginClick : LoginEvent
    data object ClearError : LoginEvent
}

data class LoginState(
    val isLoading: Boolean = false,
    val identifier: String = "",
    val password: String = "",
)

sealed interface LoginEffect {
    data object NavigateToMain : LoginEffect
    data class ShowToastError(val message: String) : LoginEffect
}
