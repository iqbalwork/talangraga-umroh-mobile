package com.talangraga.umrohmobile.presentation.login

sealed class LoginEffect {
    data class ShowError(val message: String) : LoginEffect()
    data object NavigateToHome : LoginEffect()
}