package com.talangraga.umrohmobile.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.talangraga.umrohmobile.data.local.database.TalangragaDatabase
import com.talangraga.umrohmobile.data.local.database.getDatabaseBuilder
import com.talangraga.umrohmobile.data.local.database.getRoomDatabase
import createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DataStore<Preferences>> { createDataStore(androidContext()) }
        single<HttpClientEngine> { OkHttp.create() }
        single<TalangragaDatabase> {
            val builder = getDatabaseBuilder(androidContext())
            getRoomDatabase(builder)
        }
    }