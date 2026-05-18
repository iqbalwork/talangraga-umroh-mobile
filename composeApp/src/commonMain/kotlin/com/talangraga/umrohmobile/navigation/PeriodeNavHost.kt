package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talangraga.umrohmobile.presentation.periode.PeriodeScreen

@Composable
fun PeriodeNavHost(
    navController: NavHostController,
    rootNavController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.PERIODE
    ) {
        composable(Screen.BottomNavItem.PERIODE) {
            PeriodeScreen(
                navHostController = navController
            )
        }
    }
}
