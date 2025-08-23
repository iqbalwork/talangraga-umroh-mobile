package com.talangraga.umrohmobile.module

import com.talangraga.umrohmobile.data.network.HttpClientFactory
import com.talangraga.umrohmobile.data.network.api.AuthService
import com.talangraga.umrohmobile.data.repository.AuthRepositoryImpl
import com.talangraga.umrohmobile.domain.repository.AuthRepository
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single {
        HttpClientFactory.create(get(), get())
    }
    single {
        AuthService(get())
    }
    single<Json> {
        Json {
            isLenient = true // Optional: if you want to be lenient with JSON parsing
            ignoreUnknownKeys =
                true // Optional: if you want to ignore keys not defined in your data classes
            // Add other Json configurations as needed
        }
    }
    single<AuthRepository> {
        AuthRepositoryImpl(
            authService = get(),
            json = get(),
            sessionStore = get()
        )
    }
}