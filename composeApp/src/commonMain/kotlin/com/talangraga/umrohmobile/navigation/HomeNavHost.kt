package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.talangraga.umrohmobile.presentation.home.HomeScreen
import com.talangraga.umrohmobile.presentation.transaction.addtransaction.AddTransactionScreen
import com.talangraga.umrohmobile.presentation.transaction.detailtransaction.TransactionDetailScreen
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import kotlinx.serialization.json.Json

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

        composable<Screen.AddTransactionRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.AddTransactionRoute>()
            AddTransactionScreen(navController, args.isCollective)
        }

        composable<Screen.TransactionDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TransactionDetailRoute>()
            val transaction = Json.decodeFromString<TransactionUiData>(args.transactionJson)
            TransactionDetailScreen(
                transaction = transaction,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
