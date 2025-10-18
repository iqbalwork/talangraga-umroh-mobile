package com.talangraga.umrohmobile

import androidx.compose.ui.window.ComposeUIViewController
import com.talangraga.umrohmobile.module.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}
