@file:Suppress("AssignedValueIsNeverRead")

package com.talangraga.umrohmobile.presentation.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.talangraga.umrohmobile.navigation.BottomNavRoute
import com.talangraga.umrohmobile.navigation.HomeNavHost
import com.talangraga.umrohmobile.navigation.MemberNavHost
import com.talangraga.umrohmobile.navigation.PeriodeNavHost
import com.talangraga.umrohmobile.navigation.ProfileNavHost
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.navigation.TransactionNavHost
import com.talangraga.umrohmobile.ui.component.ImageViewerManager
import com.talangraga.umrohmobile.ui.utils.isWideScreen

@Composable
fun MainScreen(rootNavHostController: NavHostController) {
    val isWide = isWideScreen()

    // child nav controllers for tabs
    val homeNav = rememberNavController()
    val transactionNav = rememberNavController()
    val periodeNav = rememberNavController()
    val memberNav = rememberNavController()
    val profileNav = rememberNavController()

    var selectedTabRoute by rememberSaveable { mutableStateOf("Home") }
    val selectedTab = when (selectedTabRoute) {
        "Transaction" -> BottomNavRoute.Transaction
        "Periode" -> BottomNavRoute.Periode
        "Member" -> BottomNavRoute.Member
        "Profile" -> BottomNavRoute.Profile
        else -> BottomNavRoute.Home
    }
    val onSelectTab: (BottomNavRoute) -> Unit = { route ->
        selectedTabRoute = when (route) {
            BottomNavRoute.Transaction -> "Transaction"
            BottomNavRoute.Periode -> "Periode"
            BottomNavRoute.Member -> "Member"
            BottomNavRoute.Profile -> "Profile"
            BottomNavRoute.Home -> "Home"
        }
    }

    // Observe current route of member nav to hide bottom bar
    val homeBackStackEntry by homeNav.currentBackStackEntryAsState()
    val homeCurrentRoute = homeBackStackEntry?.destination?.route
    val transactionBackStackEntry by transactionNav.currentBackStackEntryAsState()
    val transactionCurrentRoute = transactionBackStackEntry?.destination?.route
    val periodeBackStackEntry by periodeNav.currentBackStackEntryAsState()
    val periodeCurrentRoute = periodeBackStackEntry?.destination?.route
    val memberBackStackEntry by memberNav.currentBackStackEntryAsState()
    val memberCurrentRoute = memberBackStackEntry?.destination?.route
    val profileBackStackEntry by profileNav.currentBackStackEntryAsState()
    val profileCurrentRoute = profileBackStackEntry?.destination?.route

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
    ) == true || memberCurrentRoute?.contains(
        Screen.UserRoute::class.simpleName ?: "UserRoute"
    ) == true || profileCurrentRoute?.contains(
        Screen.AddUserRoute::class.simpleName ?: "AddUserRoute"
    ) == true || homeCurrentRoute?.contains(
        Screen.AddTransactionRoute::class.simpleName ?: "AddTransactionRoute"
    ) == true || homeCurrentRoute?.contains(
        Screen.TransactionDetailRoute::class.simpleName ?: "TransactionDetailRoute"
    ) == true || transactionCurrentRoute?.contains(
        Screen.AddTransactionRoute::class.simpleName ?: "AddTransactionRoute"
    ) == true || transactionCurrentRoute?.contains(
        Screen.TransactionDetailRoute::class.simpleName ?: "TransactionDetailRoute"
    ) == true

    val showNav = !isSubScreen && !ImageViewerManager.isVisible

    if (isWide) {
        // 💻 Tablet / Wide Screen Layout with Navigation Rail
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = showNav,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                AdaptiveNavRail(
                    selected = selectedTab,
                    onSelect = onSelectTab
                )
            }

            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                TabContent(
                    selectedTab = selectedTab,
                    homeNav = homeNav,
                    transactionNav = transactionNav,
                    periodeNav = periodeNav,
                    memberNav = memberNav,
                    profileNav = profileNav,
                    rootNavHostController = rootNavHostController,
                    onSelectTab = onSelectTab
                )
            }
        }
    } else {
        // 📱 Phone / Compact Screen with Telegram-Style Floating Bottom Navigation Bar
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Tab Nav Host
            TabContent(
                selectedTab = selectedTab,
                homeNav = homeNav,
                transactionNav = transactionNav,
                periodeNav = periodeNav,
                memberNav = memberNav,
                profileNav = profileNav,
                rootNavHostController = rootNavHostController,
                onSelectTab = onSelectTab
            )

            // Telegram-style Floating Bottom Navigation Bar
            AnimatedVisibility(
                visible = showNav,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(200)
                ) + fadeOut(animationSpec = tween(150)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            ) {
                FloatingBottomNavBar(
                    selected = selectedTab,
                    onSelect = onSelectTab
                )
            }
        }
    }
}

@Composable
private fun TabContent(
    selectedTab: BottomNavRoute,
    homeNav: NavHostController,
    transactionNav: NavHostController,
    periodeNav: NavHostController,
    memberNav: NavHostController,
    profileNav: NavHostController,
    rootNavHostController: NavHostController,
    onSelectTab: (BottomNavRoute) -> Unit
) {
    AnimatedContent(
        targetState = selectedTab,
        transitionSpec = {
            fadeIn(tween(150)) togetherWith fadeOut(tween(150))
        },
        modifier = Modifier.fillMaxSize()
    ) { tab ->
        when (tab) {
            BottomNavRoute.Home -> HomeNavHost(
                navController = homeNav,
                rootNavController = rootNavHostController,
            ) {
                onSelectTab(BottomNavRoute.Transaction)
            }

            BottomNavRoute.Transaction -> TransactionNavHost(
                navController = transactionNav,
                rootNavController = rootNavHostController
            )

            BottomNavRoute.Periode -> PeriodeNavHost(
                navController = periodeNav,
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
