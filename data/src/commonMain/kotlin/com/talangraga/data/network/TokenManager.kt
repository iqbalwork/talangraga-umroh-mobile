package com.talangraga.data.network

import com.talangraga.data.local.session.Session
import com.talangraga.data.local.session.SessionKey
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TokenManager(): KoinComponent {

    private val session: Session by inject()

    private val _tokenFlow = MutableStateFlow(session.getString(SessionKey.TOKEN_KEY))
    val tokenFlow: StateFlow<String> = _tokenFlow
//
    private val _refreshTokenFlow =
        MutableStateFlow(session.getString(SessionKey.REFRESH_TOKEN_KEY))
    val refreshTokenFlow: StateFlow<String> = _refreshTokenFlow

    private val _logoutEvent = Channel<Unit>()
    val logoutEvent = _logoutEvent.receiveAsFlow()

    fun saveAccessToken(token: String) {
        session.saveString(SessionKey.TOKEN_KEY, token)
        _tokenFlow.value = token
    }

    fun saveRefreshToken(refreshToken: String) {
        session.saveString(SessionKey.REFRESH_TOKEN_KEY, refreshToken)
        _refreshTokenFlow.value = refreshToken
    }

    fun clearToken() {
        session.remove(SessionKey.TOKEN_KEY)
        session.remove(SessionKey.REFRESH_TOKEN_KEY)
        _tokenFlow.value = ""
        _refreshTokenFlow.value = ""
    }

    fun logout() {
        clearToken()
        session.remove(SessionKey.IS_LOGGED_IN)
        session.remove(SessionKey.PROFILE_KEY)
        _logoutEvent.trySend(Unit)
    }

    fun getAccessToken(): String {
        return tokenFlow.value
    }

    fun getRefreshToken(): String {
        return refreshTokenFlow.value
    }
}
