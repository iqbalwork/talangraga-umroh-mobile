package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talangraga.umrohmobile.presentation.home.HomeScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    rootNavController: NavHostController,
    onNavigateToTransaction: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.HOME
    ) {
        composable(Screen.BottomNavItem.HOME) {
            HomeScreen(
                navHostController = navController,
                rootNavHostController = rootNavController,
                onNavigateToTransaction = onNavigateToTransaction
            )
        }
    }
}
