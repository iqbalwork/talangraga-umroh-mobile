package com.talangraga.umrohmobile.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    object BottomNavItem {
        const val HOME = "home_screen"
        const val TRANSACTION = "transaction_screen"
        const val PERIODE = "periode_screen"
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
    data class AddUserRoute(val userId: String = "", val isEdit: Boolean, val isLoginUser: Boolean)

    @Serializable
    data class EditProfileRoute(val userId: String = "", val isLoginUser: Boolean)

    @Serializable
    data class MemberDetailRoute(val userId: String)

    @Serializable
    data class ChangePasswordRoute(val userId: String)

    @Serializable
    data class TransactionDetailRoute(val transactionJson: String)

    @Serializable
    data class HomeRoute(val justLogin: Boolean = false)

    @Serializable
    object PeriodeRoute

    @Serializable
    object ListUserRoute

    @Serializable
    data class UserRoute(val userId: String, val isLoginUser: Boolean)
}
