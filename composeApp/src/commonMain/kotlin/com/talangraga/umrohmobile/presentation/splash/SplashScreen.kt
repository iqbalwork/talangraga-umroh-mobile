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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.data.local.session.SessionKey
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.presentation.navigation.MainRoute
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

    val isLogin = viewModel.session.getBoolean(SessionKey.IS_LOGGED_IN)

    LaunchedEffect(isLogin) {
        delay(1000)
        if (!isLogin) {
            navHostController.navigate(LoginRoute) {
                popUpTo(SplashRoute) { inclusive = true }
            }
        } else {
            // navigate to MainRoute’s HOME tab
            navHostController.navigate(MainRoute.route) {
                popUpTo(SplashRoute) { inclusive = true }
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
