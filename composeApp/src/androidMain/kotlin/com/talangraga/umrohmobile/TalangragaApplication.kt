package com.talangraga.umrohmobile

import android.app.Application
import com.talangraga.umrohmobile.module.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class TalangragaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TalangragaApplication)
            analytics {
                setApiKey("ktz-sdk-nO-XzT4ULQfLeuf4f3PYTCRGtGYmJHtvP3_D4S_v078")
            }
        }
        Napier.base(DebugAntilog())
    }

}
