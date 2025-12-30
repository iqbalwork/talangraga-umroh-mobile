package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talangraga.shared.navigation.Screen
import com.talangraga.umrohmobile.presentation.transaction.TransactionScreen

@Composable
fun TransactionNavHost(navController: NavHostController, rootNavController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.TRANSACTION
    ) {
        composable(Screen.BottomNavItem.TRANSACTION) {
            TransactionScreen(
                navHostController = navController,
                rootNavController = rootNavController,
            )
        }

    }
}
