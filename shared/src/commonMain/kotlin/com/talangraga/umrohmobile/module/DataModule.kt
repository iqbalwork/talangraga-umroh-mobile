package com.talangraga.umrohmobile.module

import com.talangraga.umrohmobile.data.AppSession
import com.talangraga.umrohmobile.data.appSessionStore
import com.talangraga.umrohmobile.repository.SessionRepository
import com.talangraga.umrohmobile.repository.SessionRepositoryImpl
import io.github.xxfast.kstore.KStore
import org.koin.dsl.module

val dataModule = module {
    single<KStore<AppSession>> {
        appSessionStore
    }
    single<SessionRepository> { SessionRepositoryImpl(store = get()) }
}