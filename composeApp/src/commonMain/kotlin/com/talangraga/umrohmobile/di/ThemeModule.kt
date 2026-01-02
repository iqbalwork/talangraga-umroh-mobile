package com.talangraga.umrohmobile.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.talangraga.umrohmobile.ui.theme.ThemeManager
import com.talangraga.umrohmobile.ui.theme.ThemePreference
import org.koin.dsl.module

val themeModule = module {
    single<ObservableSettings> { Settings() as ObservableSettings }   // not FlowSettings
    single { ThemePreference(get()) }
    single { ThemeManager(get()) }
}
