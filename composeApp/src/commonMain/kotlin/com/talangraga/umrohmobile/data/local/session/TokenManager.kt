package com.talangraga.umrohmobile.data.local.session

import SessionStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenManager(private val dataStore: SessionStore) {

    private val TOKEN_KEY = stringPreferencesKey(DataStoreKey.TOKEN_KEY)

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }
}