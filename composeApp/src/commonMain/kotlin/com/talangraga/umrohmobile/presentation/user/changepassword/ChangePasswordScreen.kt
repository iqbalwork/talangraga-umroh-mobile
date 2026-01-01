package com.talangraga.umrohmobile.presentation.user.changepassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.shared.Background
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.ui.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.PasswordInput
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun ChangePasswordScreen(
    navHostController: NavHostController,
    userId: Int,
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    val currentPassword by viewModel.currentPassword.collectAsStateWithLifecycle()
    val newPassword by viewModel.newPassword.collectAsStateWithLifecycle()
    val confirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()

    ChangePasswordContent(
        onBackClick = { navHostController.popBackStack() },
        currentPassword = currentPassword,
        onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
        newPassword = newPassword,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onClickChangePassword = viewModel::changePassword
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordContent(
    onBackClick: () -> Unit,
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
                title = { Text("Ganti Kata Sandi", style = TalangragaTypography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        },
        containerColor = Background,
        bottomBar = {
            Button(
                onClick = onClickChangePassword,
                colors = ButtonDefaults.buttonColors(containerColor = Sage),
                enabled = currentPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank() && (confirmPassword == newPassword),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Simpan kata sandi")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PasswordInput(
                title = "Kata sandi lama",
                password = currentPassword,
                onPasswordChange = onCurrentPasswordChange,
                placeholder = "Masukkan kata sandi lama Anda"
            )

            PasswordInput(
                title = "Kata sandi baru",
                password = newPassword,
                onPasswordChange = onNewPasswordChange,
                placeholder = "Masukkan kata sandi baru Anda"
            )

            if (newPassword.isNotEmpty() && newPassword.length < 8) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Kata sandi minimal 8 karakter",
                        style = TalangragaTypography.bodySmall.copy(color = Color.Red)
                    )
                }
            }

            PasswordInput(
                title = "Konfirmasi kata sandi baru",
                password = confirmPassword,
                onPasswordChange = onConfirmPasswordChange,
                placeholder = "Konfirmasi kata sandi baru Anda"
            )

            if (confirmPassword.isNotEmpty() && confirmPassword.length < 8) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Kata sandi minimal 8 karakter",
                        style = TalangragaTypography.bodySmall.copy(color = Color.Red)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewChangePasswordContent() {
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
