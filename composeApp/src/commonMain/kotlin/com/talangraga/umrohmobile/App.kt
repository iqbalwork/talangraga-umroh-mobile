package com.talangraga.umrohmobile

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.talangraga.umrohmobile.presentation.home.HomeScreen
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.navigation.*
import com.talangraga.umrohmobile.presentation.splash.SplashScreen
import com.talangraga.umrohmobile.presentation.user.ListUserScreen
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.user.profile.ProfileScreen
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.ThemeManager
import com.talangraga.umrohmobile.ui.ThemeMode
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject
import kotlin.reflect.typeOf



// -------------------------------------------------------------
// Bottom Navigation Routes
// -------------------------------------------------------------
sealed class BottomNavRoute(val route: String) {
    data object Home : BottomNavRoute("home")
    data object Member : BottomNavRoute("members")
    data object Profile : BottomNavRoute("profile")
}


// -------------------------------------------------------------
// Bottom Navigation Bar
// -------------------------------------------------------------
@Composable
fun BottomNavBar(
    selected: BottomNavRoute,
    onSelect: (BottomNavRoute) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selected is BottomNavRoute.Home,
            onClick = { onSelect(BottomNavRoute.Home) },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected is BottomNavRoute.Member,
            onClick = { onSelect(BottomNavRoute.Member) },
            icon = { Icon(Icons.Default.Group, null) },
            label = { Text("Member") }
        )
        NavigationBarItem(
            selected = selected is BottomNavRoute.Profile,
            onClick = { onSelect(BottomNavRoute.Profile) },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") }
        )
    }
}



// -------------------------------------------------------------
// APP ROOT
// -------------------------------------------------------------
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

    // Splash/Login NavController (Top-level)
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
                    composable("main_container") {

                        AnimatedContent(
                            targetState = selectedTab,
                            transitionSpec = {
                                fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                            }
                        ) { tab ->

                            when (tab) {
                                BottomNavRoute.Home -> HomeNavHost(homeNav)
                                BottomNavRoute.Member -> MemberNavHost(memberNav)
                                BottomNavRoute.Profile -> ProfileNavHost(profileNav)
                            }
                        }
                    }
                }

                // Redirect to main container after login
                LaunchedEffect(currentRoute) {
                    if (currentRoute == LoginRoute::class.qualifiedName) {
                        // After login redirect:
                        // Splash -> Login -> Main
                        rootNavController.navigate("main_container") {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}



// -------------------------------------------------------------
// INDIVIDUAL NAV HOSTS (one per bottom tab)
// -------------------------------------------------------------

@Composable
fun HomeNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home_screen"
    ) {
        composable("home_screen") {
            HomeScreen(navHostController = navController, justLogin = false)
        }
    }
}

@Composable
fun MemberNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "member_screen"
    ) {
        composable("member_screen") {
            ListUserScreen(navHostController = navController)
        }
    }
}

@Composable
fun ProfileNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "profile_screen"
    ) {
        composable("profile_screen") {
            ProfileScreen(
                navHostController = navController,
                user = null,
                isLoginUser = true
            )
        }
    }
}



// -------------------------------------------------------------
// Serializable Type Helper
// -------------------------------------------------------------
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
