package com.talangraga.umrohmobile.presentation.navigation

import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object LoginRoute

@Serializable
data class HomeRoute(val justLogin: Boolean = false)

@Serializable
object ListUserRoute

@Serializable
data class UserRoute(val user: UserUIData, val isLoginUser: Boolean)
