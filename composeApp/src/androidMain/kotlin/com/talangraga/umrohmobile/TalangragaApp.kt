package com.talangraga.umrohmobile

import android.app.Application
import com.talangraga.umrohmobile.module.dataModule
import com.talangraga.umrohmobile.module.sharedModule
import com.talangraga.umrohmobile.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class TalangragaApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initNapier()
    }

    private fun initNapier() {

    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidContext(this@TalangragaApp)
            modules(dataModule, sharedModule, viewModelModule)
        }
    }
}