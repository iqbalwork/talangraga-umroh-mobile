package com.talangraga.umrohmobile.data.local.session

import SessionStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@Composable
fun SessionStore.getBoolean(key: String, initialValue: Boolean? = null): State<Boolean?> {
    return data.map {
        val booleanKey = booleanPreferencesKey(key)
        it[booleanKey]
    }.collectAsState(initial = initialValue)
}

suspend fun SessionStore.saveBoolean(key: String, value: Boolean) {
    edit {
        val booleanKey = booleanPreferencesKey(key)
        it[booleanKey] = value
    }
}

@Composable
fun SessionStore.getString(key: String, initialValue: String = ""): State<String> {
    return data.map {
        val stringKey = stringPreferencesKey(key)
        it[stringKey].orEmpty()
    }.collectAsState(initial = initialValue)
}

fun SessionStore.getUserProfile(): Flow<UserResponse?> {
    return data.map { preferences ->
        val stringKey = stringPreferencesKey(DataStoreKey.PROFILE_KEY)
        val profileString = preferences[stringKey]
        val profile: UserResponse? = if (profileString != null) {
            Json.decodeFromString(UserResponse.serializer(), profileString)
        } else {
            null
        }
        profile
    }
}

fun SessionStore.getToken(): Flow<String?> {
    return data.map { preferences ->
        val stringKey = stringPreferencesKey(DataStoreKey.TOKEN_KEY)
        preferences[stringKey]
    }
}

suspend fun SessionStore.saveToken(token: String) {
    edit {
        val stringKey = stringPreferencesKey(DataStoreKey.TOKEN_KEY)
        it[stringKey] = token
    }
}

suspend fun SessionStore.fetchString(key: String): String? {
    return data.map { preferences ->
        val stringKey = stringPreferencesKey(key)
        preferences[stringKey]
    }.firstOrNull()
}

// Ensure DataStoreKey.TOKEN_KEY is accessible here
// For example, by importing it if it's in a different package/file
suspend fun SessionStore.fetchToken(): String {
    return fetchString(DataStoreKey.TOKEN_KEY).orEmpty()
}

suspend fun SessionStore.saveString(key: String, value: String) {
    edit {
        val stringKey = stringPreferencesKey(key)
        it[stringKey] = value
    }
}

suspend fun SessionStore.saveInt(key: String, value: Int) {
    edit {
        val intKey = intPreferencesKey(key)
        it[intKey] = value
    }
}

suspend fun SessionStore.clearAll() {
    edit {
        it.clear()
    }
}

