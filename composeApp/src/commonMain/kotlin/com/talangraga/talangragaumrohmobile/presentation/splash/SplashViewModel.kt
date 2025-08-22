package com.talangraga.talangragaumrohmobile.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.talangragaumrohmobile.data.local.loginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(): ViewModel() {

    private val _isLogin = MutableStateFlow<Boolean?>(null)
    val isLogin = _isLogin

    fun getLoginState() {
        viewModelScope.launch {
            _isLogin.value = loginState.get()
        }
    }

}