package com.talangraga.umrohmobile

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.talangraga.umrohmobile.di.initializeKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class TalangragaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TalangragaApplication)
            analytics {
                setApiKey(BuildKonfig.KOTZILLA_KEY)
            }
        }
        Firebase.initialize(this)
        Napier.base(DebugAntilog())
    }
}
