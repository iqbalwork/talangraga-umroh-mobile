package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talangraga.shared.navigation.Screen
import com.talangraga.umrohmobile.presentation.user.ListUserScreen

@Composable
fun MemberNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.MEMBER
    ) {
        composable(Screen.BottomNavItem.MEMBER) {
            ListUserScreen(navHostController = navController)
        }
    }
}
