package com.talangraga.umrohmobile

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.talangraga.umrohmobile.presentation.home.HomeScreen
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.presentation.navigation.SplashRoute
import com.talangraga.umrohmobile.presentation.splash.SplashScreen

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SplashRoute) {
        composable<SplashRoute> { SplashScreen(navHostController = navController) }
        composable<LoginRoute> { LoginScreen(navHostController = navController) }
        composable<HomeRoute> { HomeScreen(navHostController = navController) }
    }
}