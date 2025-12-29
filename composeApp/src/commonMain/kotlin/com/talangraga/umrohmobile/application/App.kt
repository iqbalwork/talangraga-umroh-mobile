package com.talangraga.umrohmobile.application

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.talangraga.data.network.TokenManager
import com.talangraga.shared.navigation.Screen
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.main.MainScreen
import com.talangraga.umrohmobile.presentation.splash.SplashScreen
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.ThemeManager
import com.talangraga.umrohmobile.ui.ThemeMode
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
                composable(Screen.MainRoute.route) {
                    MainScreen(
                        rootNavHostController = rootNavController
                    )
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
