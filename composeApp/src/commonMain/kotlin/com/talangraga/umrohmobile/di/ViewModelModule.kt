package com.talangraga.umrohmobile.di

import com.talangraga.umrohmobile.AppViewModel
import com.talangraga.umrohmobile.presentation.home.HomeViewModel
import com.talangraga.umrohmobile.presentation.login.LoginViewModel
import com.talangraga.umrohmobile.presentation.splash.SplashViewModel
import com.talangraga.umrohmobile.presentation.user.ListUserViewModel
import com.talangraga.umrohmobile.presentation.user.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(session = get()) }
    viewModel { LoginViewModel(repository = get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ListUserViewModel(repository = get()) }
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AppViewModel)
}
