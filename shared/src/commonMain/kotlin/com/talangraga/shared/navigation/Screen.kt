package com.talangraga.shared.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    object BottomNavItem {
        const val HOME = "home_screen"
        const val TRANSACTION = "transaction_screen"
        const val MEMBER = "member_screen"
        const val PROFILE = "profile_screen"
    }

    @Serializable
    object SplashRoute

    @Serializable
    object LoginRoute

    @Serializable
    object MainRoute {
        const val ROUTE = "main_route"
    }

    @Serializable
    data class AddTransactionRoute(val isCollective: Boolean = false)

    @Serializable
    object AddUserRoute

    @Serializable
    data class EditProfileRoute(val userId: Int, val isLoginUser: Boolean)

    @Serializable
    data class HomeRoute(val justLogin: Boolean = false)

    @Serializable
    object ListUserRoute

    @Serializable
    data class UserRoute(val userId: Int, val isLoginUser: Boolean)
}
