package com.talangraga.talangragaumrohmobile.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

expect fun getClientEngine(): HttpClientEngine

val networkModule = module {
    single {
        HttpClient(getClientEngine()) {
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
            defaultRequest {
                url("http://192.168.101.8:1337/api/")
            }
        }
    }
    single {
        AuthService(get())
    }
}
