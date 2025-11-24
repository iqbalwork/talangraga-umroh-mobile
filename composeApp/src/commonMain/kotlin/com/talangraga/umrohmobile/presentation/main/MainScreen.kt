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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.talangraga.umrohmobile.presentation.navigation.BottomNavRoute
import com.talangraga.umrohmobile.presentation.navigation.HomeNavHost
import com.talangraga.umrohmobile.presentation.navigation.MemberNavHost
import com.talangraga.umrohmobile.presentation.navigation.ProfileNavHost
import com.talangraga.umrohmobile.presentation.navigation.TransactionNavHost

@Composable
fun MainScreen(rootNavHostController: NavHostController, modifier: Modifier = Modifier) {

    // child nav controllers for tabs
    val homeNav = rememberNavController()
    val transactionNav = rememberNavController()
    val memberNav = rememberNavController()
    val profileNav = rememberNavController()

    var selectedTab by remember { mutableStateOf<BottomNavRoute>(BottomNavRoute.Home) }

    Scaffold(
        bottomBar = {
            BottomNavBar(selectedTab) { selectedTab = it }
        }
    ) { paddingValues ->

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                fadeIn(tween(150)) togetherWith fadeOut(tween(150))
            },
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
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

                BottomNavRoute.Member -> MemberNavHost(navController = memberNav)

                BottomNavRoute.Profile -> ProfileNavHost(navController = profileNav)
            }
        }
    }
}