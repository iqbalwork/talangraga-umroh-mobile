package com.talangraga.umrohmobile.module

import com.talangraga.umrohmobile.data.network.AuthService
import com.talangraga.umrohmobile.data.network.getClientEngine
import com.talangraga.umrohmobile.repository.AuthRepository
import com.talangraga.umrohmobile.repository.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule = module {
    single {
        HttpClient(getClientEngine()) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP Client: $message")
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    single { TokenManager() }

    single { AuthService(httpClient = get()) }

    single { AuthRepository(authService = get(), tokenManager = get()) }
}