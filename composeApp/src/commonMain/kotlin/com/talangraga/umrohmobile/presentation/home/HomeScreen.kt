package com.talangraga.umrohmobile.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {

    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    HomeContent(
        profile = profile
    ) {
        viewModel.clearSession()
        navHostController.navigate(LoginRoute) {
            popUpTo(HomeRoute) {
                inclusive = true
            }
        }
    }
}

@Composable
fun HomeContent(
    profile: UserResponse?,
    onLogout: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Welcome to Home Screen ${profile?.fullname.orEmpty()}")
            Button(onClick = onLogout) {
                Text("Logout")
            }

        }
    }
}