package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.talangraga.umrohmobile.presentation.transaction.TransactionScreen
import com.talangraga.umrohmobile.presentation.transaction.addtransaction.AddTransactionScreen

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

        composable<Screen.AddTransactionRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.AddTransactionRoute>()
            AddTransactionScreen(navController, args.isCollective)
        }

    }
}
