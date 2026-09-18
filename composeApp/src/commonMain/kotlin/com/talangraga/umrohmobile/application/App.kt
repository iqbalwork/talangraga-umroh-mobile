package com.talangraga.umrohmobile.application

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.talangraga.data.network.TokenManager
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.presentation.login.LoginScreen
import com.talangraga.umrohmobile.presentation.main.MainScreen
import com.talangraga.umrohmobile.presentation.splash.SplashScreen
import com.talangraga.umrohmobile.presentation.transaction.addtransaction.AddTransactionScreen
import com.talangraga.umrohmobile.presentation.transaction.detailtransaction.TransactionDetailScreen
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.adduser.AddUserScreen
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import com.talangraga.umrohmobile.ui.theme.ThemeManager
import com.talangraga.umrohmobile.ui.theme.ThemeMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.backhandler.BackHandler
import com.talangraga.umrohmobile.ui.component.ImageViewerManager
import com.talangraga.umrohmobile.ui.component.ZoomableCoilImage

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun App() {

    val themeManager: ThemeManager = koinInject()
    val tokenManager: TokenManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()
    val isDynamicColor by themeManager.isDynamicColor.collectAsState()

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

    Crossfade(targetState = isDarkTheme, animationSpec = tween(400)) { targetDark ->
        TalangragaTheme(
            darkTheme = targetDark,
            useDynamicColor = isDynamicColor
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
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
                    composable(Screen.MainRoute.ROUTE) {
                        MainScreen(
                            rootNavHostController = rootNavController
                        )
                    }

                    composable<Screen.AddTransactionRoute> { backStackEntry ->
                        val args = backStackEntry.toRoute<Screen.AddTransactionRoute>()
                        AddTransactionScreen(rootNavController, args.isCollective)
                    }

                    composable<Screen.AddUserRoute> { backStackEntry ->
                        val args = backStackEntry.toRoute<Screen.AddUserRoute>()
                        AddUserScreen(
                            navController = rootNavController,
                            isEdit = args.isEdit,
                            userId = args.userId,
                            isLoginUser = args.isLoginUser
                        )
                    }

                    composable<Screen.TransactionDetailRoute> { backStackEntry ->
                        val args = backStackEntry.toRoute<Screen.TransactionDetailRoute>()
                        val transaction = Json.decodeFromString<TransactionUiData>(args.transactionJson)
                        TransactionDetailScreen(
                            transaction = transaction,
                            onBackClick = { rootNavController.popBackStack() }
                        )
                    }
                }

                // Global Fullscreen Image Viewer Overlay
                val activeImage = ImageViewerManager.activeImage
                var backgroundAlpha by remember { mutableStateOf(1f) }

                LaunchedEffect(activeImage) {
                    if (activeImage != null) backgroundAlpha = 1f
                }

                BackHandler(enabled = activeImage != null) {
                    ImageViewerManager.hide()
                }

                AnimatedVisibility(
                    visible = activeImage != null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(99f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = backgroundAlpha))
                    ) {
                        if (activeImage != null) {
                            ZoomableCoilImage(
                                model = activeImage,
                                onDismiss = {
                                    ImageViewerManager.hide()
                                },
                                onDragChange = { progress ->
                                    backgroundAlpha = (1f - progress * 4).coerceIn(0f, 1f)
                                }
                            )
                        }

                        if (backgroundAlpha > 0.8f) {
                            IconButton(
                                onClick = { ImageViewerManager.hide() },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                                    .statusBarsPadding()
                            ) {
                                Icon(Icons.Default.Close, "Close", tint = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
