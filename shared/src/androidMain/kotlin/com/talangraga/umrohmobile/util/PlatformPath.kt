package com.talangraga.umrohmobile.util

import android.app.Application
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// Helper to get Context via Koin, assuming Application is registered
object AndroidContextProvider : KoinComponent {
    val context: Application by inject()
}

actual fun getDataStoreDirectoryPath(): String {
    return AndroidContextProvider.context.filesDir.path
}
