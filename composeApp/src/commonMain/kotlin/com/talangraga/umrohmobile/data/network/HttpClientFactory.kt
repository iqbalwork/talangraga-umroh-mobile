package com.talangraga.umrohmobile.data.network

import com.talangraga.umrohmobile.BuildKonfig
import com.talangraga.umrohmobile.data.local.session.TokenManager
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import sp.bvantur.inspektify.ktor.AutoDetectTarget
import sp.bvantur.inspektify.ktor.InspektifyKtor

object HttpClientFactory {

    fun create(
        engine: HttpClientEngine,
        tokenManager: TokenManager
    ): HttpClient {

        val authPlugin = createClientPlugin("AuthPlugin") {
            onRequest { request, _ ->
                val token = tokenManager.getAccessToken() // suspend works here
                if (token.isNotBlank()) {
                    request.headers.append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

        return HttpClient(engine) {
            install(InspektifyKtor) {
                logLevel = sp.bvantur.inspektify.ktor.LogLevel.All
                autoDetectEnabledFor = setOf(AutoDetectTarget.Android, AutoDetectTarget.Apple)
                shortcutEnabled = true
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "KTOR", message = message)
                    }
                }
                level = LogLevel.ALL
            }
            install(authPlugin)
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenManager.getAccessToken()
                        val refreshToken = tokenManager.getRefreshToken()
                        BearerTokens(accessToken, refreshToken)
                    }
                    refreshTokens {
                        val refreshToken = tokenManager.getRefreshToken()

                        // Call FastAPI /auth/refresh
                        val response = client.post("https://talangraga.com/auth/refresh") {
                            header("Authorization", "Bearer $refreshToken")
                        }

                        if (response.status.isSuccess()) {
                            val json = response.body<JsonObject>()
                            val newAccessToken =
                                json["data"]?.jsonObject?.get("access_token")?.jsonPrimitive?.content

                            if (newAccessToken != null) {
                                // Save the new token
                                tokenManager.saveAccessToken(newAccessToken)

                                // Return new BearerTokens for Ktor to retry the original request
                                BearerTokens(newAccessToken, refreshToken)
                            } else null
                        } else null
                    }
                }
            }
//            install(KtorMonitorLogging) {
//                sanitizeHeader { header -> header == "Authorization" }
//                filter { request -> !request.url.host.contains("cosminmihu.ro") }
//                showNotification = true
//                retentionPeriod = RetentionPeriod.OneHour
//                maxContentLength = ContentLength.Default
//            }
            defaultRequest {
                url(BuildKonfig.BASE_URL)
            }
        }
    }

}
