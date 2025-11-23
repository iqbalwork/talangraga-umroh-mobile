package com.talangraga.umrohmobile.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.AppViewModel
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.presentation.navigation.MainRoute
import com.talangraga.umrohmobile.ui.Sage
import com.talangraga.umrohmobile.ui.SageDark
import com.talangraga.umrohmobile.ui.Sandstone
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.TalangragaTypography
import com.talangraga.umrohmobile.ui.TextSecondaryDark
import com.talangraga.umrohmobile.ui.component.InputText
import com.talangraga.umrohmobile.ui.component.PasswordInput
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.bg_screen
import talangragaumrohmobile.composeapp.generated.resources.input_here
import talangragaumrohmobile.composeapp.generated.resources.input_password_here
import talangragaumrohmobile.composeapp.generated.resources.label_username_or_email
import talangragaumrohmobile.composeapp.generated.resources.login
import talangragaumrohmobile.composeapp.generated.resources.password
import talangragaumrohmobile.composeapp.generated.resources.talangraga_logo

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    viewModel: LoginViewModel = koinViewModel(),
    appViewModel: AppViewModel = koinViewModel(),
) {

    val isDarkMode by appViewModel.isDarkMode.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val loginSucceed by viewModel.loginSucceed.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(loginSucceed) {
        if (loginSucceed == true) {
            navHostController.navigate(MainRoute.route) {
                popUpTo(LoginRoute) { inclusive = true }
            }
        }
    }

    Crossfade(targetState = isDarkMode, animationSpec = tween(600)) {
        LoginContent(
            isDarkMode = isDarkMode,
            isLoading = isLoading,
            errorMessage = errorMessage.orEmpty(),
            identifier = viewModel.identifier.value,
            password = viewModel.password.value,
            onIdentifierChange = viewModel::onIdentifierChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::login
        )
    }
}

@Composable
fun LoginContent(
    isDarkMode: Boolean,
    isLoading: Boolean = false,
    errorMessage: String,
    identifier: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    onIdentifierChange: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    TalangragaTheme(darkTheme = isDarkMode, useDynamicColor = false) {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(SageDark, Color.White),
                            start = Offset(0f, 0f), // Top-left
                            end = Offset(
                                0f,
                                Float.POSITIVE_INFINITY
                            ) // Bottom-right
                        )
                    )
                    .imePadding(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.bg_screen),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = 0.45f),
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.talangraga_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        "Masuk",
                        style = TalangragaTypography.titleMedium.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "Masuk via email, nomor hp, atau username",
                        style = TalangragaTypography.titleMedium.copy(
                            fontSize = 16.sp,
                            color = TextSecondaryDark,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    InputText(
                        title = stringResource(Res.string.label_username_or_email),
                        value = identifier,
                        onValueChange = onIdentifierChange,
                        placeholder = stringResource(Res.string.input_here),
                        leadingIcon = Icons.Filled.AccountCircle,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PasswordInput(
                        title = stringResource(Res.string.password),
                        password = password,
                        onPasswordChange = onPasswordChange,
                        placeholder = stringResource(Res.string.input_password_here),
                        leadingIcon = Icons.Filled.Security,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (!isLoading) onLoginClick()
                        },
                        enabled = identifier.isNotBlank() && password.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AnimatedVisibility(visible = isLoading) {
                                CircularProgressIndicator(
                                    color = Sandstone,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                stringResource(Res.string.login),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }

                    if (errorMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginContentPreview() {
    TalangragaTheme(darkTheme = false, useDynamicColor = false) {
        LoginContent(
            isDarkMode = false,
            isLoading = false,
            errorMessage = "Invalid credentials",
            identifier = "testuser",
            password = "password",
            onPasswordChange = {},
            onIdentifierChange = {},
            onLoginClick = {}
        )
    }
}
