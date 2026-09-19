package com.talangraga.umrohmobile.presentation.user.changepassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.ui.component.LoadingButton
import com.talangraga.umrohmobile.ui.component.PasswordInput
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun ChangePasswordScreen(
    navHostController: NavHostController,
    userId: String,
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.errorMessage) {
        if (!uiState.errorMessage.isNullOrEmpty()) {
            ToastManager.show(message = uiState.errorMessage.orEmpty(), type = ToastType.Error)
            viewModel.onEvent(ChangePasswordEvent.ClearError)
        }
    }

    LaunchedEffect(uiState.isPasswordChanged) {
        if (uiState.isPasswordChanged) {
            scope.launch {
                ToastManager.show(message = "Kata sandi berhasil diubah", type = ToastType.Success)
                delay(2000)
                navHostController.popBackStack()
            }
        }
    }

    ChangePasswordContent(
        onBackClick = { navHostController.popBackStack() },
        isLoading = uiState.isLoading,
        currentPassword = uiState.currentPassword,
        onCurrentPasswordChange = { viewModel.onEvent(ChangePasswordEvent.OnCurrentPasswordChange(it)) },
        newPassword = uiState.newPassword,
        onNewPasswordChange = { viewModel.onEvent(ChangePasswordEvent.OnNewPasswordChange(it)) },
        confirmPassword = uiState.confirmPassword,
        onConfirmPasswordChange = { viewModel.onEvent(ChangePasswordEvent.OnConfirmPasswordChange(it)) },
        onClickChangePassword = { viewModel.onEvent(ChangePasswordEvent.ChangePassword) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordContent(
    onBackClick: () -> Unit,
    isLoading: Boolean = false,
    currentPassword: String,
    onCurrentPasswordChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onClickChangePassword: () -> Unit
) {
    TalangragaScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ganti Kata Sandi",
                        style = TalangragaTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LoadingButton(
                    onClick = onClickChangePassword,
                    enabled = currentPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank() && (confirmPassword == newPassword),
                    isLoading = isLoading,
                    modifier = Modifier
                        .widthIn(max = 640.dp)
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Simpan Kata Sandi"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PasswordInput(
                    title = "Kata sandi lama",
                    password = currentPassword,
                    onPasswordChange = onCurrentPasswordChange,
                    placeholder = "Masukkan kata sandi lama Anda",
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )

                PasswordInput(
                    title = "Kata sandi baru",
                    password = newPassword,
                    onPasswordChange = onNewPasswordChange,
                    placeholder = "Masukkan kata sandi baru Anda",
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )

                if (newPassword.isNotEmpty() && newPassword.length < 8) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Kata sandi minimal 8 karakter",
                            style = TalangragaTypography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }
                }

                PasswordInput(
                    title = "Konfirmasi kata sandi baru",
                    password = confirmPassword,
                    onPasswordChange = onConfirmPasswordChange,
                    placeholder = "Konfirmasi kata sandi baru Anda",
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )

                if (confirmPassword.isNotEmpty() && confirmPassword.length < 8) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Kata sandi minimal 8 karakter",
                            style = TalangragaTypography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewChangePasswordContent() {
    TalangragaTheme {
        ChangePasswordContent(
            onBackClick = { },
            currentPassword = "",
            onCurrentPasswordChange = { },
            newPassword = "",
            onNewPasswordChange = { },
            confirmPassword = "",
            onConfirmPasswordChange = { },
            onClickChangePassword = { }
        )
    }
}
