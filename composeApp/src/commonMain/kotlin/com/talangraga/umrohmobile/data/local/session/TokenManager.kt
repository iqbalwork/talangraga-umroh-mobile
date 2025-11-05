package com.talangraga.umrohmobile.data.local.session

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TokenManager(): KoinComponent {

    private val session: Session by inject()

    fun saveAccessToken(token: String) {
        session.saveString(SessionKey.TOKEN_KEY, token)
    }

    fun saveRefreshToken(refreshToken: String) {
        session.saveString(SessionKey.REFRESH_TOKEN_KEY, refreshToken)
    }

    fun clearToken() {
        session.remove(SessionKey.TOKEN_KEY)
    }

    fun getAccessToken(): String {
        return session.getString(SessionKey.TOKEN_KEY, "")
    }

    fun getRefreshToken(): String {
        return session.getString(SessionKey.REFRESH_TOKEN_KEY, "")
    }
}
