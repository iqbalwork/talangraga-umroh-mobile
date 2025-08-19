package com.talangraga.umrohmobile.repository

import com.talangraga.umrohmobile.util.getDataStoreDirectoryPath
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import kotlinx.serialization.Serializable

@Serializable
data class TokenWrapper(val token: String?)

private const val TOKEN_FILE_NAME = "auth_token.json"

class TokenManager {

    private val tokenStore: KStore<TokenWrapper> = storeOf(
        file = Path("${getDataStoreDirectoryPath()}/$TOKEN_FILE_NAME"),
        default = TokenWrapper(null)
    )

    suspend fun saveToken(token: String) {
        tokenStore.set(TokenWrapper(token))
    }

    suspend fun getToken(): String? {
        return tokenStore.get()?.token
    }

    suspend fun clearToken() {
        tokenStore.set(TokenWrapper(null)) // or tokenStore.delete() if you want to remove the file
    }

    // Optional: A flow to observe token changes if needed elsewhere
    // val tokenFlow: Flow<String?> = tokenStore.updates.map { it?.token }
}
