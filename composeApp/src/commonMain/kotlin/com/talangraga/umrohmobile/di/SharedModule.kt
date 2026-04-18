package com.talangraga.umrohmobile.di

import com.russhwolf.settings.Settings
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.HttpClientFactory
import com.talangraga.data.network.RefreshTokenHandler
import com.talangraga.data.network.TokenManager
import com.talangraga.data.network.api.ApiService
import com.talangraga.data.repository.RepositoryImpl
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<Json> {
        Json {
            isLenient = true
            ignoreUnknownKeys = true

        }
    }
    single { Settings() }
    single { Session(get(), get()) }
    single { TokenManager() }
    singleOf(::RefreshTokenHandler)
    single { HttpClientFactory.create(get(), get(), get()) }
    single {
        ApiService(get())
    }
    single<Repository> {
        RepositoryImpl(
            apiService = get(),
            session = get(),
            tokenManager = get(),
            databaseHelper = get(),
        )
    }
}
