package com.talangraga.umrohmobile.presentation.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.ui.component.InputText
import com.talangraga.umrohmobile.ui.component.LoadingButton
import com.talangraga.umrohmobile.ui.component.PasswordInput
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import com.talangraga.umrohmobile.ui.utils.isWideScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
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
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToMain -> {
                    navHostController.navigate(Screen.MainRoute.ROUTE) {
                        popUpTo(Screen.LoginRoute) { inclusive = true }
                    }
                }
                is LoginEffect.ShowToastError -> {
                    ToastManager.show(message = effect.message, type = ToastType.Error)
                    viewModel.onEvent(LoginEvent.ClearError)
                }
            }
        }
    }

    LoginContent(
        isLoading = uiState.isLoading,
        identifier = uiState.identifier,
        password = uiState.password,
        onIdentifierChange = { viewModel.onEvent(LoginEvent.OnIdentifierChange(it)) },
        onPasswordChange = { viewModel.onEvent(LoginEvent.OnPasswordChange(it)) },
        onLoginClick = { viewModel.onEvent(LoginEvent.OnLoginClick) }
    )
}

@Composable
fun LoginContent(
    isLoading: Boolean = false,
    identifier: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    onIdentifierChange: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    val isWide = isWideScreen()

    TalangragaScaffold { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.bg_screen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0.25f),
            )

            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 440.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isWide) 6.dp else 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.talangraga_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(130.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Masuk",
                        style = TalangragaTypography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Masuk via email, nomor hp, atau username",
                        style = TalangragaTypography.bodyMedium.copy(
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    InputText(
                        title = stringResource(Res.string.label_username_or_email),
                        value = identifier,
                        onValueChange = onIdentifierChange,
                        placeholder = stringResource(Res.string.input_here),
                        leadingIcon = Icons.Filled.AccountCircle,
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordInput(
                        title = stringResource(Res.string.password),
                        password = password,
                        onPasswordChange = onPasswordChange,
                        placeholder = stringResource(Res.string.input_password_here),
                        leadingIcon = Icons.Filled.Security,
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    LoadingButton(
                        modifier = Modifier.fillMaxWidth(),
                        isLoading = isLoading,
                        text = stringResource(Res.string.login),
                        enabled = identifier.isNotBlank() && password.isNotBlank(),
                        onClick = onLoginClick
                    )
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
            isLoading = false,
            identifier = "testuser",
            password = "password",
            onPasswordChange = {},
            onIdentifierChange = {},
            onLoginClick = {}
        )
    }
}
