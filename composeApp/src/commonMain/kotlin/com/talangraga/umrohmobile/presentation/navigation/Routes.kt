package com.talangraga.umrohmobile.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object LoginRoute

@Serializable
data class HomeRoute(val justLogin: Boolean = false)

@Serializable
object ListUserRoute