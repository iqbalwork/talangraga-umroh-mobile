package com.talangraga.umrohmobile

import androidx.compose.ui.window.ComposeUIViewController
import com.talangraga.umrohmobile.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) {
    App()
}
