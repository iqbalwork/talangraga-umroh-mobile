package com.talangraga.talangragaumrohmobile

import android.app.Application
import com.talangraga.talangragaumrohmobile.module.initKoin

class TalangragaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

}