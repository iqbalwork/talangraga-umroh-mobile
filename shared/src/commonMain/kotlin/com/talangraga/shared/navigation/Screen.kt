package com.talangraga.shared.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    object BottomNavItem {
        const val HOME = "home"
        const val MEMBER = "member"
        const val PROFILE = "profile"
    }

    @Serializable
    object SplashRoute

    @Serializable
    object LoginRoute

    @Serializable
    object MainRoute {
        val route = "main_route"
    }

    @Serializable
    data class HomeRoute(val justLogin: Boolean = false)

    @Serializable
    object ListUserRoute

    @Serializable
    data class UserRoute(val userId: Int, val isLoginUser: Boolean)
}
