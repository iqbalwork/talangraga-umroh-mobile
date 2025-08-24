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
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.data.local.session.DataStoreKey
import com.talangraga.umrohmobile.data.local.session.getBoolean
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.presentation.navigation.SplashRoute
import com.talangraga.umrohmobile.ui.TalangragaTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.talangraga_logo

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: SplashViewModel = koinViewModel()
) {

    val isLogin by viewModel.sessionStore.getBoolean(DataStoreKey.IS_LOGGED_IN)

    LaunchedEffect(isLogin) {
        println("SplashScreen Navigation: isLogin = $isLogin")
        delay(1000)
        if (isLogin == null || isLogin == false) {
            navHostController.navigate(LoginRoute) {
                popUpTo(SplashRoute) { inclusive = true }
            }
            println("Navigation: Splash to Login")
        } else {
            navHostController.navigate(HomeRoute()) {
                popUpTo(SplashRoute) { inclusive = true }
            }
            println("Navigation: Splash to Home")
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
