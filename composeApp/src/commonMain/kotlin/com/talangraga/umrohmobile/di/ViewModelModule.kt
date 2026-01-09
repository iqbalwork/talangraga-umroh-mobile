package com.talangraga.umrohmobile.di

import com.talangraga.umrohmobile.presentation.home.HomeViewModel
import com.talangraga.umrohmobile.presentation.home.member.MemberDetailViewModel
import com.talangraga.umrohmobile.presentation.login.LoginViewModel
import com.talangraga.umrohmobile.presentation.splash.SplashViewModel
import com.talangraga.umrohmobile.presentation.transaction.TransactionViewModel
import com.talangraga.umrohmobile.presentation.transaction.addtransaction.AddTransactionViewModel
import com.talangraga.umrohmobile.presentation.user.ListUserViewModel
import com.talangraga.umrohmobile.presentation.user.adduser.AddUserViewModel
import com.talangraga.umrohmobile.presentation.user.changepassword.ChangePasswordViewModel
import com.talangraga.umrohmobile.presentation.user.editprofile.EditProfileViewModel
import com.talangraga.umrohmobile.presentation.user.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ListUserViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AddUserViewModel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::MemberDetailViewModel)
    viewModelOf(::ChangePasswordViewModel)
    viewModelOf(::TransactionViewModel)
    viewModelOf(::AddTransactionViewModel)
}
