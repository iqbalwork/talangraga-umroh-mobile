package com.talangraga.umrohmobile.di

import com.talangraga.data.local.database.DatabaseHelper
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseHelper(get()) }
}
