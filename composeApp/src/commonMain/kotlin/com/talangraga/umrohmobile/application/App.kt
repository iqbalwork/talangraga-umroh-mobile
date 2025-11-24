package com.talangraga.umrohmobile.application

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.talangraga.shared.navigation.Screen
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.main.BottomNavBar
import com.talangraga.umrohmobile.presentation.navigation.BottomNavRoute
import com.talangraga.umrohmobile.presentation.navigation.HomeNavHost
import com.talangraga.umrohmobile.presentation.navigation.MemberNavHost
import com.talangraga.umrohmobile.presentation.navigation.ProfileNavHost
import com.talangraga.umrohmobile.presentation.navigation.TransactionNavHost
import com.talangraga.umrohmobile.presentation.splash.SplashScreen
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.ThemeManager
import com.talangraga.umrohmobile.ui.ThemeMode
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {

    val themeManager: ThemeManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()

    val systemDark = isSystemInDarkTheme()
    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    // NavController (Top-level)
    val rootNavController = rememberNavController()

    Crossfade(targetState = systemDark, animationSpec = tween(400)) {
        TalangragaTheme(
            darkTheme = isDarkTheme,
            useDynamicColor = false
        ) {

            val navBackStack by rootNavController.currentBackStackEntryAsState()
            val currentRoute = navBackStack?.destination?.route

            val showBottomBar = currentRoute !in listOf(
                Screen.SplashRoute::class.qualifiedName,
                Screen.LoginRoute::class.qualifiedName
            )

            // Multi-backstack: 3 NavControllers (one per tab)
            val homeNav = rememberNavController()
            val transactionNav = rememberNavController()
            val memberNav = rememberNavController()
            val profileNav = rememberNavController()

            var selectedTab by remember { mutableStateOf<BottomNavRoute>(BottomNavRoute.Home) }

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        BottomNavBar(selectedTab) { selectedTab = it }
                    }
                }
            ) { padding ->

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
                    composable(Screen.MainRoute.route) {
                        AnimatedContent(
                            targetState = selectedTab,
                            transitionSpec = {
                                fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                            }
                        ) { tab ->

                            when (tab) {
                                BottomNavRoute.Home -> HomeNavHost(
                                    homeNav,
                                    rootNavController = rootNavController
                                ) {
                                    selectedTab = BottomNavRoute.Transaction
                                }

                                BottomNavRoute.Transaction -> TransactionNavHost(
                                    navController = transactionNav,
                                    rootNavController
                                )

                                BottomNavRoute.Member -> MemberNavHost(memberNav)
                                BottomNavRoute.Profile -> ProfileNavHost(profileNav)
                            }
                        }
                    }
                }
            }
        }
    }
}

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed) {

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write { putString(key, json.encodeToString(value)) }
    }

    override fun get(bundle: SavedState, key: String): T? {
        return json.decodeFromString<T?>(bundle.read { getString(key) })
    }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)
}
