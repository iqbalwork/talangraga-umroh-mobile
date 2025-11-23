package com.talangraga.umrohmobile.presentation.navigation

sealed class BottomNavRoute(val route: String) {
    data object Home : BottomNavRoute("home")
    data object Member : BottomNavRoute("members")
    data object Profile : BottomNavRoute("profile")
}
