package com.talangraga.talangragaumrohmobile.module

import com.talangraga.talangragaumrohmobile.data.network.networkModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            networkModule, repositoryModule, viewModelModule
        )
    }
}
