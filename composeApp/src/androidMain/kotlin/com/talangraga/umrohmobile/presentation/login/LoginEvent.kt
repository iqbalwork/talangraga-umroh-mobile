package com.talangraga.umrohmobile.presentation.login

sealed class LoginEvent {
    data class OnLoginClick(val identifier: String, val password: String) : LoginEvent()
}