package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talangraga.shared.navigation.Screen
import com.talangraga.umrohmobile.presentation.user.ListUserScreen
import com.talangraga.umrohmobile.presentation.user.adduser.AddUserScreen

@Composable
fun MemberNavHost(navController: NavHostController, rootNavController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.MEMBER
    ) {
        composable(Screen.BottomNavItem.MEMBER) {
            ListUserScreen(rootNavController = rootNavController, navHostController = navController)
        }
        composable<Screen.AddUserRoute> {
            AddUserScreen(navController = navController)
        }
    }
}
