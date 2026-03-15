package com.talangraga.umrohmobile.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.local.session.Session
import com.talangraga.data.local.session.SessionKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val session: Session
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

    init {
        onEvent(SplashEvent.CheckLoginStatus)
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckLoginStatus -> {
                viewModelScope.launch {
                    delay(1000)
                    val isLogin = session.getBoolean(SessionKey.IS_LOGGED_IN)
                    if (isLogin) {
                        _effect.emit(SplashEffect.NavigateToMain)
                    } else {
                        _effect.emit(SplashEffect.NavigateToLogin)
                    }
                }
            }
        }
    }
}
