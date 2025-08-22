package com.talangraga.talangragaumrohmobile.module

import com.talangraga.talangragaumrohmobile.data.repository.AuthRepositoryImpl
import com.talangraga.talangragaumrohmobile.domain.repository.AuthRepository
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val repositoryModule = module {
    single<Json> {
        Json {
            isLenient = true // Optional: if you want to be lenient with JSON parsing
            ignoreUnknownKeys = true // Optional: if you want to ignore keys not defined in your data classes
            // Add other Json configurations as needed
        }
    }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}
