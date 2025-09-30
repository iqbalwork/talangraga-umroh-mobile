package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.TalangragaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogoutDialog(
    onDismissRequest: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    val fontFamily = TalangragaTypography

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Konfirmasi Logout",
                        style = TalangragaTypography.titleMedium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = onDismissRequest, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Dialog"
                        )
                    }
                }

                Text(
                    text = "Apakah Anda yakin ingin keluar dari akun ini?",
                    style = TalangragaTypography.bodySmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onConfirmLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F) // Red color
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Logout",
                        style = TalangragaTypography.bodyMedium
                    )
                }

                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults
                        .outlinedButtonBorder(enabled = true)
                        .copy(width = 1.dp)
                ) {
                    Text(
                        text = "Batal",
                        style = TalangragaTypography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LogoutDialogPreview() {
    TalangragaTheme {
        // To make the preview background visible if the dialog itself is white
        Column(Modifier.background(Color.Black.copy(alpha = 0.5f)).padding(16.dp)) {
            LogoutDialog(
                onDismissRequest = {},
                onConfirmLogout = {}
            )
        }
    }
}
