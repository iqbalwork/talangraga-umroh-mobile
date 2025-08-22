package com.talangraga.talangragaumrohmobile.module

import com.talangraga.talangragaumrohmobile.presentation.login.LoginViewModel
import com.talangraga.talangragaumrohmobile.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { LoginViewModel(authRepository = get()) }
}