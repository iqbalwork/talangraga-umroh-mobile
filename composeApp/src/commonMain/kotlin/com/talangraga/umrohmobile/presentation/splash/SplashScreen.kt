package com.talangraga.umrohmobile.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.talangraga_logo

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: SplashViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SplashEffect.NavigateToMain -> {
                    navHostController.navigate(Screen.MainRoute.ROUTE) {
                        popUpTo(Screen.SplashRoute) { inclusive = true }
                    }
                }
                is SplashEffect.NavigateToLogin -> {
                    navHostController.navigate(Screen.LoginRoute) {
                        popUpTo(Screen.SplashRoute) { inclusive = true }
                    }
                }
            }
        }
    }

    SplashContent()
}

@Composable
@Preview
fun SplashContentPreview() {
    SplashContent()
}

@Composable
fun SplashContent() {
    TalangragaTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.talangraga_logo),
                contentDescription = "Splash Screen Image",
            )
        }
    }
}
