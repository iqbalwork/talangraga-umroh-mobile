package com.talangraga.umrohmobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talangraga.umrohmobile.presentation.user.ListUserScreen

@Composable
fun MemberNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "member_screen"
    ) {
        composable("member_screen") {
            ListUserScreen(navHostController = navController)
        }
    }
}
