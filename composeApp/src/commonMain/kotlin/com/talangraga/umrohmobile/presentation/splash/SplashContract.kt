package com.talangraga.umrohmobile.presentation.splash

sealed interface SplashEvent {
    data object CheckLoginStatus : SplashEvent
}

data class SplashState(
    val isLoading: Boolean = true
)

sealed interface SplashEffect {
    data object NavigateToMain : SplashEffect
    data object NavigateToLogin : SplashEffect
}
