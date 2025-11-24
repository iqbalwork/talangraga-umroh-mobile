package com.talangraga.umrohmobile.presentation.navigation

import kotlinx.serialization.Serializable

sealed class BottomNavRoute() {
    @Serializable
    data object Home : BottomNavRoute()
    @Serializable
    data object Transaction : BottomNavRoute()
    @Serializable
    data object Member : BottomNavRoute()
    @Serializable
    data object Profile : BottomNavRoute()
}
