package com.talangraga.umrohmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.talangraga.umrohmobile.presentation.user.adduser.AddUserScreen
import com.talangraga.umrohmobile.presentation.user.changepassword.ChangePasswordScreen
import com.talangraga.umrohmobile.presentation.user.editprofile.EditProfileScreen
import com.talangraga.umrohmobile.presentation.user.profile.ProfileScreen

@Composable
fun ProfileNavHost(rootNavController: NavHostController, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.PROFILE
    ) {
        composable(Screen.BottomNavItem.PROFILE) {
            ProfileScreen(
                rootNavHostController = rootNavController,
                navHostController = navController,
                isLoginUser = true
            )
        }
        composable<Screen.AddUserRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.AddUserRoute>()
            AddUserScreen(
                navController = navController,
                isEdit = args.isEdit,
                userId = args.userId,
                isLoginUser = args.isLoginUser
            )
        }
        composable<Screen.EditProfileRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditProfileRoute>()
            EditProfileScreen(
                navHostController = navController,
                userId = args.userId,
                isLoginUser = args.isLoginUser,
            )
        }
        composable<Screen.ChangePasswordRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ChangePasswordRoute>()
            ChangePasswordScreen(
                navHostController = navController,
                userId = args.userId
            )
        }
    }
}
