package com.talangraga.talangragaumrohmobile.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.talangraga.talangragaumrohmobile.presentation.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: LoginViewModel = koinViewModel()
        val loginState by viewModel.loginState.collectAsStateWithLifecycle()

        LoginContent(
            modifier = Modifier,
            loginState = loginState,
            onLoginClick = { username, password ->
                viewModel.login(username, password)
                // For example, by observing loginState and navigating when it's LoginState.Success.
                navigator.push(HomeScreen)
            }
        )
    }
}

@Preview
@Composable
fun LoginContentPreview() {
    // For preview, you might need a dummy LoginComponent or pass initial LoginState directly
    LoginContent(loginState = LoginState.Idle, onLoginClick = { _, _ -> })
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    loginState: LoginState,
    onLoginClick: (String, String) -> Unit
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val isLoading = loginState is LoginState.Loading

    MaterialTheme {
        Scaffold(modifier = modifier) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Your Logo Here", // Replace with your actual logo
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username or Email") },
                        singleLine = true,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        enabled = !isLoading,
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisibility)
                                Icons.Filled.VisibilityOff
                            else Icons.Filled.Visibility

                            val description = if (passwordVisibility) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(imageVector = image, description)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            onLoginClick(username, password)
                        },
                        enabled = username.isNotBlank() && password.isNotBlank() && !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Login")
                    }

                    if (loginState is LoginState.Error) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = loginState.errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                if (isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
