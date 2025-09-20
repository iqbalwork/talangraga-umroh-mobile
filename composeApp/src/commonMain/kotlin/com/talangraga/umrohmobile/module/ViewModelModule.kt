package com.talangraga.umrohmobile.module

import com.talangraga.umrohmobile.presentation.home.HomeViewModel
import com.talangraga.umrohmobile.presentation.login.LoginViewModel
import com.talangraga.umrohmobile.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(sessionStore = get()) }
    viewModel { LoginViewModel(repository = get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
}