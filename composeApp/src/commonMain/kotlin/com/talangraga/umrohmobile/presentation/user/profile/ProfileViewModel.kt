package com.talangraga.umrohmobile.presentation.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.TokenManager
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val session: Session,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _profile = MutableStateFlow(session.getProfile()?.toUiData())
    val profile = _profile.asStateFlow()

    fun clearSession() {
        viewModelScope.launch {
            session.clear()
            tokenManager.clearToken()
        }
    }

}
