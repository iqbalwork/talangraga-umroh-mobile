package com.talangraga.umrohmobile.presentation.navigation

import com.talangraga.umrohmobile.data.local.database.model.UserEntity
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
data class UserRoute(val user: UserEntity, val isLoginUser: Boolean)
