package com.talangraga.umrohmobile.application

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.talangraga.umrohmobile.presentation.navigation.BottomNavRoute
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.main.BottomNavBar
import com.talangraga.umrohmobile.presentation.navigation.*
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
                SplashRoute::class.qualifiedName,
                LoginRoute::class.qualifiedName
            )

            // Multi-backstack: 3 NavControllers (one per tab)
            val homeNav = rememberNavController()
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
                    startDestination = SplashRoute
                ) {

                    composable<SplashRoute> {
                        SplashScreen(rootNavController)
                    }

                    composable<LoginRoute> {
                        LoginScreen(rootNavController)
                    }

                    // MAIN CONTENT AREA (Persistent)
                    composable(MainRoute.route) {
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
                                )

                                BottomNavRoute.Member -> MemberNavHost(memberNav)
                                BottomNavRoute.Profile -> ProfileNavHost(profileNav)
                            }
                        }
                    }
                }

//                // Redirect to main container after login
//                LaunchedEffect(currentRoute) {
//                    if (currentRoute == LoginRoute::class.qualifiedName) {
//                        // After login redirect:
//                        // Splash -> Login -> Main
//                        rootNavController.navigate(MainRoute.route) {
//                            popUpTo(SplashRoute) { inclusive = true }
//                        }
//                    }
//                }
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
