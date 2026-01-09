package com.talangraga.umrohmobile.presentation.user.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.TokenManager
import kotlinx.coroutines.launch

class ProfileViewModel(
    val session: Session,
    private val tokenManager: TokenManager,
) : ViewModel() {

    val imageUrl = mutableStateOf(session.userProfile.value?.imageProfile)

    fun onImageChange(uri: String) {
        imageUrl.value = uri
    }

    fun clearSession() {
        viewModelScope.launch {
            session.clear()
            tokenManager.clearToken()
        }
    }

}
