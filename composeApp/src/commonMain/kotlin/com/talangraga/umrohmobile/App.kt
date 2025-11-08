package com.talangraga.umrohmobile

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.presentation.home.HomeScreen
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.ListUserRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.presentation.navigation.SplashRoute
import com.talangraga.umrohmobile.presentation.navigation.UserRoute
import com.talangraga.umrohmobile.presentation.splash.SplashScreen
import com.talangraga.umrohmobile.presentation.user.ListUserScreen
import com.talangraga.umrohmobile.presentation.user.profile.ProfileScreen
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.ThemeManager
import com.talangraga.umrohmobile.ui.ThemeMode
import com.talangraga.umrohmobile.ui.animateColorSchemeAsState
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject
import kotlin.reflect.typeOf

@Composable
fun App() {
    val navController = rememberNavController()
    val themeManager: ThemeManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()
    val systemDark = isSystemInDarkTheme()

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    Crossfade(targetState = systemDark, animationSpec = tween(400)) { isDark ->
        TalangragaTheme(
            darkTheme = isDarkTheme,
            useDynamicColor = false
        ) {
            NavHost(
                navController = navController,
                startDestination = SplashRoute,
//        enterTransition = { fadeIn() },
//        exitTransition = { fadeOut() },
//        popEnterTransition = { slideInHorizontally() },
//        popExitTransition = { slideOutHorizontally() }
            ) {
                composable<SplashRoute> { SplashScreen(navHostController = navController) }
                composable<LoginRoute> {
                    LoginScreen(
                        navHostController = navController,
                    )
                }
                composable<HomeRoute> { backStackEntry ->
                    val homeArgs = backStackEntry.toRoute<HomeRoute>()
                    val justLogin = homeArgs.justLogin
                    HomeScreen(
                        navHostController = navController,
                        justLogin = justLogin,
                    )
                }
                composable<ListUserRoute> {
                    ListUserScreen(
                        navHostController = navController
                    )
                }
                composable<UserRoute>(
                    typeMap = mapOf(
                        typeOf<UserEntity>() to serializableType<UserEntity>()
                    )
                ) { backstackEntry ->
                    val args = backstackEntry.toRoute<UserRoute>()
                    val userEntity = args.user
                    val isLoginUser = args.isLoginUser
                    ProfileScreen(
                        navHostController = navController,
                        user = userEntity,
                        isLoginUser = isLoginUser,
                    )
                }
            }
        }
    }
}

/**
 * composable<SearchBoxScreen>(
 *     typeMap = mapOf(
 *         typeOf<SearchType>() to serializableType<SearchType>()
 *     )
 * ) { backStackEntry ->
 *     val args = backStackEntry.toRoute<SearchBoxScreen>()
 *     SearchScreen(args = args, navController = navCtl)
 * }
 */

// Source - https://stackoverflow.com/a/79773267
// Posted by BenjyTec
// Retrieved 2025-11-08, License - CC BY-SA 4.0

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write { putString(key, json.encodeToString(value)) }
    }

    override fun get(bundle: SavedState, key: String): T? {
        return json.decodeFromString<T?>(bundle.read { getString(key) })
    }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)
}

