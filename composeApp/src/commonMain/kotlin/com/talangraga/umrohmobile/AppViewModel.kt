package com.talangraga.umrohmobile

import androidx.lifecycle.ViewModel
import com.talangraga.umrohmobile.data.local.session.Session
import com.talangraga.umrohmobile.data.local.session.SessionKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel(
    private val session: Session
): ViewModel() {

    private val _isDarkMode = MutableStateFlow(session.getBoolean(SessionKey.DARK_MODE_KEY))
    val isDarkMode = _isDarkMode.asStateFlow()

    fun onDarkModeChange(isDarkMode: Boolean) {
        session.saveBoolean(SessionKey.DARK_MODE_KEY, isDarkMode)
        _isDarkMode.update { isDarkMode }
    }

}
