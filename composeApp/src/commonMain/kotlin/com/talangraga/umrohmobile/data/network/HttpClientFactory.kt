package com.talangraga.umrohmobile.data.network

import com.talangraga.umrohmobile.BuildKonfig
import com.talangraga.umrohmobile.data.local.session.TokenManager
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import sp.bvantur.inspektify.ktor.AutoDetectTarget
import sp.bvantur.inspektify.ktor.InspektifyKtor

object HttpClientFactory {

    fun create(
        engine: HttpClientEngine,
        tokenManager: TokenManager
    ): HttpClient {
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
            install(Auth) {
                bearer {
                    loadTokens {
                        tokenManager.getToken()?.let {
                            BearerTokens(it, null)
                        }
                    }
                }
            }
//            install(KtorMonitorLogging) {
////                sanitizeHeader { header -> header == "Authorization" }
////                filter { request -> !request.url.host.contains("cosminmihu.ro") }
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