@file:Suppress("AssignedValueIsNeverRead")

package com.talangraga.umrohmobile.presentation.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.talangraga.umrohmobile.navigation.BottomNavRoute
import com.talangraga.umrohmobile.navigation.HomeNavHost
import com.talangraga.umrohmobile.navigation.MemberNavHost
import com.talangraga.umrohmobile.navigation.ProfileNavHost
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.navigation.TransactionNavHost

@Composable
fun MainScreen(rootNavHostController: NavHostController) {

    // child nav controllers for tabs
    val homeNav = rememberNavController()
    val transactionNav = rememberNavController()
    val memberNav = rememberNavController()
    val profileNav = rememberNavController()

    var selectedTab by remember { mutableStateOf<BottomNavRoute>(BottomNavRoute.Home) }

    // Observe current route of member nav to hide bottom bar
    val memberBackStackEntry by memberNav.currentBackStackEntryAsState()
    val memberCurrentRoute = memberBackStackEntry?.destination?.route
    val profileBackStackEntry by profileNav.currentBackStackEntryAsState()
    val profileCurrentRoute = profileBackStackEntry?.destination?.route

    // Check if the current route in member nav is AddUserRoute
    // Note: Type-safe navigation routes might look different, 
    // but usually contain the qualified name or the route string.
    // For safety, we can check if it matches the AddUserRoute pattern.
    // However, since we are using Type-Safe navigation, 'route' property might be null or complex string.
    // A simpler way with type safe nav is checking the destination class name if available or similar logic.
    // For now, let's assume standard behavior where nested destinations are identifiable.

    // Actually, simply checking if we are NOT at start destination of Member tab
    val isSubScreen = memberCurrentRoute?.contains(
        Screen.AddUserRoute::class.simpleName ?: "AddUserRoute"
    ) == true || memberCurrentRoute?.contains(
        Screen.EditProfileRoute::class.simpleName ?: "EditProfileRoute"
    ) == true || profileCurrentRoute?.contains(
        Screen.EditProfileRoute::class.simpleName ?: "EditProfileRoute"
    ) == true || profileCurrentRoute?.contains(
        Screen.ChangePasswordRoute::class.simpleName ?: "ChangePasswordRoute"
    ) == true || memberCurrentRoute?.contains(
        Screen.MemberDetailRoute::class.simpleName ?: "MemberDetailRoute"
    ) == true || profileCurrentRoute?.contains(
        Screen.AddUserRoute::class.simpleName ?: "AddUserRoute"
    ) == true

    val showBottomBar = !isSubScreen

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(selectedTab) { selectedTab = it }
            }
        }
    ) { paddingValues ->

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                fadeIn(tween(150)) togetherWith fadeOut(tween(150))
            },
            modifier = Modifier.padding(bottom = if (showBottomBar) paddingValues.calculateBottomPadding() else 0.dp)
        ) { tab ->
            when (tab) {

                BottomNavRoute.Home -> HomeNavHost(
                    navController = homeNav,
                    rootNavController = rootNavHostController,

                    ) {
                    // callback from Home tab
                    selectedTab = BottomNavRoute.Transaction
                }

                BottomNavRoute.Transaction -> TransactionNavHost(
                    navController = transactionNav,
                    rootNavController = rootNavHostController
                )

                BottomNavRoute.Member -> MemberNavHost(
                    navController = memberNav,
                    rootNavController = rootNavHostController
                )

                BottomNavRoute.Profile -> ProfileNavHost(
                    rootNavController = rootNavHostController,
                    navController = profileNav
                )
            }
        }
    }
}
