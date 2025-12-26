package com.talangraga.data.network

import com.talangraga.data.BuildKonfig
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import sp.bvantur.inspektify.ktor.AutoDetectTarget
import sp.bvantur.inspektify.ktor.InspektifyKtor

object HttpClientFactory {

    // Shared Mutex to prevent multiple concurrent refreshes
    private val refreshMutex = Mutex()

    fun create(
        engine: HttpClientEngine,
        refreshTokenHandler: RefreshTokenHandler,
        tokenManager: TokenManager
    ): HttpClient {

        val client = HttpClient(engine) {

            if (BuildKonfig.IS_DEBUG) {

                install(InspektifyKtor) {
                    logLevel = sp.bvantur.inspektify.ktor.LogLevel.All
                    autoDetectEnabledFor = setOf(AutoDetectTarget.Android, AutoDetectTarget.Apple)
                    shortcutEnabled = true
                }

                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.v(tag = "KTOR", message = message)
                        }
                    }
                    level = LogLevel.ALL
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Auth) {
                bearer {
                    // Always attach token, even if server doesn't send WWW-Authenticate
                    sendWithoutRequest { true }

                    loadTokens {
                        val access = tokenManager.getAccessToken()
                        val refresh = tokenManager.getRefreshToken()
                        if (access.isNotBlank() && refresh.isNotBlank())
                            BearerTokens(access, refresh)
                        else null
                    }

                    refreshTokens {
                        Napier.i("🔄 Refresh token triggered!")

                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken.isEmpty()) return@refreshTokens null

                        // Use mutex to ensure single refresh at a time
                        return@refreshTokens refreshMutex.withLock {
                            Napier.i("🔒 Acquired refresh mutex")

                            // Re-check if another thread already refreshed the token
                            val currentAccess = tokenManager.getAccessToken()
                            val isStillExpired =
                                currentAccess.isBlank() || currentAccess == oldTokens?.accessToken
                            if (!isStillExpired) {
                                Napier.i("✅ Token already refreshed by another coroutine")
                                return@withLock BearerTokens(currentAccess, refreshToken)
                            }

                            refreshTokenHandler.getRefreshToken(refreshToken)
                        }
                    }
                }
            }

            defaultRequest {
                url(BuildKonfig.BASE_URL)
            }
        }

        // Reactive header updates when token changes
        CoroutineScope(Dispatchers.Default).launch {
            tokenManager.tokenFlow.collectLatest { token ->
                client.config {
                    defaultRequest {
                        if (token.isNotEmpty()) {
                            header(HttpHeaders.Authorization, "Bearer $token")
                        }
                    }
                }
            }
        }

        return client
    }
}
