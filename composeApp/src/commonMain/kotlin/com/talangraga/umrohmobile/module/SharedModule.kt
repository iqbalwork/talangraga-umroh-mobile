package com.talangraga.umrohmobile.module

import com.talangraga.umrohmobile.data.local.session.TokenManager
import com.talangraga.umrohmobile.data.network.HttpClientFactory
import com.talangraga.umrohmobile.data.network.api.AuthService
import com.talangraga.umrohmobile.data.repository.RepositoryImpl
import com.talangraga.umrohmobile.domain.repository.Repository
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { TokenManager() }
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
    single<Repository> {
        RepositoryImpl(
            authService = get(),
            json = get(),
            sessionStore = get(),
            tokenManager = get(),
            databaseHelper = get(),
        )
    }
}
