package com.talangraga.umrohmobile

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.talangraga.umrohmobile.presentation.home.HomeScreen
import com.talangraga.umrohmobile.presentation.home.HomeViewModel
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.ListUserRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.presentation.navigation.SplashRoute
import com.talangraga.umrohmobile.presentation.splash.SplashScreen
import com.talangraga.umrohmobile.presentation.user.ListUserScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
//        enterTransition = { fadeIn() },
//        exitTransition = { fadeOut() },
//        popEnterTransition = { slideInHorizontally() },
//        popExitTransition = { slideOutHorizontally() }
    ) {
        composable<SplashRoute> { SplashScreen(navHostController = navController) }
        composable<LoginRoute> { LoginScreen(navHostController = navController) }
        composable<HomeRoute> { backStackEntry ->
            val viewModel: HomeViewModel = koinViewModel()
            val homeArgs = backStackEntry.toRoute<HomeRoute>()
            val justLogin = homeArgs.justLogin
            HomeScreen(
                navHostController = navController,
                justLogin = justLogin,
                viewModel = viewModel
            )
        }
        composable<ListUserRoute> { ListUserScreen(navHostController = navController) }
    }
}