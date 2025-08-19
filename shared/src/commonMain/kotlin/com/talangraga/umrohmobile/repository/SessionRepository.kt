package com.talangraga.umrohmobile.repository

// in commonMain
import com.talangraga.umrohmobile.data.AppSession
import com.talangraga.umrohmobile.data.model.UserResponse
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val sessionFlow: Flow<AppSession?>
    suspend fun login(userProfile: UserResponse)
    suspend fun logout()
}

class SessionRepositoryImpl(
    private val store: KStore<AppSession>
) : SessionRepository {

    override val sessionFlow: Flow<AppSession?> = store.updates

    override suspend fun login(userProfile: UserResponse) {
        store.set(AppSession(isLoggedIn = true, userProfile = userProfile))
    }

    override suspend fun logout() {
        store.set(AppSession()) // Reset to the default logged-out state
    }
}