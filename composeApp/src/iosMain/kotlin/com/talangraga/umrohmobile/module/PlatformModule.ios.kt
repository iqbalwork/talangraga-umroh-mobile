package com.talangraga.umrohmobile.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.talangraga.umrohmobile.data.local.database.TalangragaDatabase
import com.talangraga.umrohmobile.data.local.database.getDatabaseBuilder
import com.talangraga.umrohmobile.data.local.database.getRoomDatabase
import createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DataStore<Preferences>> { createDataStore() }
        single<HttpClientEngine> { Darwin.create() }
        single<TalangragaDatabase> {
            val builder = getDatabaseBuilder()
            getRoomDatabase(builder)
        }
    }