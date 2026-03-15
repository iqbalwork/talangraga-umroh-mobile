package com.talangraga.umrohmobile.presentation.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    val session: Session,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState(imageUrl = session.userProfile.value?.imageProfile))
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnImageChange -> _uiState.update { it.copy(imageUrl = event.uri) }
            is ProfileEvent.ClearSession -> clearSession()
        }
    }

    private fun clearSession() {
        viewModelScope.launch {
            session.clear()
            tokenManager.clearToken()
        }
    }

}
