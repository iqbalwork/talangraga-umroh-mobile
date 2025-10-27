package com.talangraga.umrohmobile.data.local.session

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TokenManager(): KoinComponent {

    private val session: Session by inject()


    fun saveToken(token: String) {
        session.saveString(SessionKey.TOKEN_KEY, token)
    }

    fun clearToken() {
        session.remove(SessionKey.TOKEN_KEY)
    }


    fun getToken(): String {
        return session.getString(SessionKey.TOKEN_KEY, "")
    }
}
