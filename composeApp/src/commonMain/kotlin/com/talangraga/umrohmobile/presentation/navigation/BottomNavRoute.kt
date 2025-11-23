package com.talangraga.umrohmobile.presentation.navigation

sealed class BottomNavRoute() {
    data object Home : BottomNavRoute()
    data object Transaction : BottomNavRoute()
    data object Member : BottomNavRoute()
    data object Profile : BottomNavRoute()
}
