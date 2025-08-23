package com.talangraga.umrohmobile

import android.app.Application
import com.talangraga.umrohmobile.module.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class TalangragaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TalangragaApplication)
        }
    }

}