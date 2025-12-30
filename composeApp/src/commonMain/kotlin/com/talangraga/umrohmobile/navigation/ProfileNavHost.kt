package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talangraga.shared.navigation.Screen
import com.talangraga.umrohmobile.presentation.user.profile.ProfileScreen

@Composable
fun ProfileNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.PROFILE
    ) {
        composable(Screen.BottomNavItem.PROFILE) {
            ProfileScreen(
                navHostController = navController,
                user = null,
                isLoginUser = true
            )
        }
    }
}
