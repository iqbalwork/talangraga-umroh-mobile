package com.talangraga.umrohmobile.module

import com.talangraga.umrohmobile.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
}