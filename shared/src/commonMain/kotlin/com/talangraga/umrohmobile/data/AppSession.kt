package com.talangraga.umrohmobile.data

import com.talangraga.umrohmobile.data.model.UserResponse
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import kotlinx.serialization.Serializable

@Serializable
data class AppSession(
    val isLoggedIn: Boolean = false,
    val userProfile: UserResponse? = null
)

val appSessionStore: KStore<AppSession> = storeOf(
    file = Path("user_session.json"),
    default = AppSession() // Provide a default, logged-out state.
)