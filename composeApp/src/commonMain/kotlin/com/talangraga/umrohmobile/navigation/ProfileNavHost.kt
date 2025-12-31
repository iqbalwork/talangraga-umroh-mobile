package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.talangraga.shared.navigation.Screen
import com.talangraga.umrohmobile.presentation.user.editprofile.EditProfileScreen
import com.talangraga.umrohmobile.presentation.user.profile.ProfileScreen

@Composable
fun ProfileNavHost(rootNavController: NavHostController, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.PROFILE
    ) {
        composable(Screen.BottomNavItem.PROFILE) {
            ProfileScreen(
                rootNavHostController = rootNavController,
                navHostController = navController,
                isLoginUser = true
            )
        }
        composable<Screen.EditProfileRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditProfileRoute>()
            EditProfileScreen(
                navHostController = navController,
                userId = args.userId,
                isLoginUser = args.isLoginUser,
            )
        }
    }
}
