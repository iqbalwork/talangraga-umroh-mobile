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
        startDestination = "home_screen"
    ) {
        composable("home_screen") {
            HomeScreen(
                navHostController = navController,
                rootNavHostController = rootNavController,
                justLogin = false,
                onNavigateToTransaction = onNavigateToTransaction
            )
        }
    }
}
