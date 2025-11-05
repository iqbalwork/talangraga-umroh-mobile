package com.talangraga.umrohmobile.module

import DriverFactory
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DataStore<Preferences>> { createDataStore() }
        single<HttpClientEngine> { Darwin.create() }
        single { DriverFactory() }
    }
