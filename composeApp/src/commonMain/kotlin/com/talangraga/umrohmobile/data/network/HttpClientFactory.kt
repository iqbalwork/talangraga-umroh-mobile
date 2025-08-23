package com.talangraga.umrohmobile.data.network

import SessionStore
import com.talangraga.umrohmobile.BuildConfig
import com.talangraga.umrohmobile.data.local.fetchToken
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(engine: HttpClientEngine, sessionStore: SessionStore): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // Added to handle potential extra fields in responses
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KTOR LOGGER: $message")
                    }
                }
                level = LogLevel.ALL
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val token = sessionStore.fetchToken().ifBlank { BuildConfig.TOKEN }
                        BearerTokens(
                            accessToken = token,
                            refreshToken = null
                        )
                    }
                }
            }
            defaultRequest {
                url("http://192.168.101.8:1337/api/")
            }
        }
    }

}