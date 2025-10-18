package com.talangraga.umrohmobile.module

import com.talangraga.umrohmobile.data.local.database.DatabaseHelper
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseHelper(get()) }
}
