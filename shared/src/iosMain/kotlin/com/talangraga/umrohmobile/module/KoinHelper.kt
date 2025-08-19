package com.talangraga.umrohmobile.module

import com.talangraga.umrohmobile.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class KoinHelper : KoinComponent {
    val authRepository: AuthRepository by inject()
}

fun initKoin() {
    startKoin {
        modules(dataModule, sharedModule)
    }
}