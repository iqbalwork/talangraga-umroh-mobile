package com.talangraga.umrohmobile.application

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.talangraga.data.network.TokenManager
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.main.MainScreen
import com.talangraga.umrohmobile.presentation.splash.SplashScreen
import com.talangraga.umrohmobile.presentation.transaction.addtransaction.AddTransactionScreen
import com.talangraga.umrohmobile.presentation.transaction.detailtransaction.TransactionDetailScreen
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.adduser.AddUserScreen
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import com.talangraga.umrohmobile.ui.theme.ThemeManager
import com.talangraga.umrohmobile.ui.theme.ThemeMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {

    val themeManager: ThemeManager = koinInject()
    val tokenManager: TokenManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()

    val systemDark = isSystemInDarkTheme()
    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    // NavController (Top-level)
    val rootNavController = rememberNavController()

    LaunchedEffect(Unit) {
        tokenManager.logoutEvent.collectLatest {
            rootNavController.navigate(Screen.LoginRoute) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Crossfade(targetState = systemDark, animationSpec = tween(400)) {
        TalangragaTheme(
            darkTheme = isDarkTheme,
            useDynamicColor = false
        ) {

//            val navBackStack by rootNavController.currentBackStackEntryAsState()
//            val currentRoute = navBackStack?.destination?.route
//
//            val showBottomBar = currentRoute !in listOf(
//                Screen.SplashRoute::class.qualifiedName,
//                Screen.LoginRoute::class.qualifiedName
//            )

            NavHost(
                navController = rootNavController,
                startDestination = Screen.SplashRoute,
            ) {

                composable<Screen.SplashRoute> {
                    SplashScreen(rootNavController)
                }

                composable<Screen.LoginRoute> {
                    LoginScreen(rootNavController)
                }

                // MAIN CONTENT AREA (Persistent)
                composable(Screen.MainRoute.ROUTE) {
                    MainScreen(
                        rootNavHostController = rootNavController
                    )
                }

                composable<Screen.AddTransactionRoute> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.AddTransactionRoute>()
                    AddTransactionScreen(rootNavController, args.isCollective)
                }

                composable<Screen.AddUserRoute> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.AddUserRoute>()
                    AddUserScreen(
                        navController = rootNavController,
                        isEdit = args.isEdit,
                        userId = args.userId,
                        isLoginUser = args.isLoginUser
                    )
                }

                composable<Screen.TransactionDetailRoute> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.TransactionDetailRoute>()
                    val transaction = Json.decodeFromString<TransactionUiData>(args.transactionJson)
                    TransactionDetailScreen(
                        transaction = transaction,
                        onBackClick = { rootNavController.popBackStack() }
                    )
                }
            }

        }
    }
}
