package com.talangraga.umrohmobile.presentation.user.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.talangraga.shared.Background
import com.talangraga.shared.BorderColor
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.TextSecondaryDark
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.ImageViewerManager
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,
    onBackClick: () -> Unit,
    viewModel: UserDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.onEvent(UserDetailEvent.GetUser(userId))
    }

    val user = uiState.user

    TalangragaScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Detail Profil", style = TalangragaTypography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Foto Profil",
                style = TalangragaTypography.bodyMedium.copy(color = TextSecondaryDark),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                    .background(Background),
                contentAlignment = Alignment.Center
            ) {
                if (user?.imageProfileUrl?.isNotBlank() == true) {
                    BasicImage(
                        model = user.imageProfileUrl,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                ImageViewerManager.show(user.imageProfileUrl)
                            }
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tidak ada foto profil",
                            style = TalangragaTypography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Informasi Profil",
                style = TalangragaTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = if (user != null) "@${user.username}" else "",
                style = TalangragaTypography.bodyMedium.copy(color = TextSecondaryDark),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Nama Lengkap",
                        style = TalangragaTypography.bodySmall.copy(color = TextSecondaryDark)
                    )
                    Text(
                        text = user?.fullname ?: "-",
                        style = TalangragaTypography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = user?.userType?.replaceFirstChar { it.uppercase() } ?: "-",
                        style = TalangragaTypography.bodyMedium
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        thickness = 1.dp,
                        color = BorderColor
                    )

                    Text(
                        text = "Kontak & Lokasi",
                        style = TalangragaTypography.bodySmall.copy(color = TextSecondaryDark)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    InfoRow(label = "Email", value = user?.email ?: "-")
                    InfoRow(label = "No. Telepon", value = user?.phone ?: "-")
                    InfoRow(label = "Domisili", value = user?.domicile ?: "-")
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = TalangragaTypography.bodySmall.copy(color = TextSecondaryDark, fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = value,
            style = TalangragaTypography.bodyMedium
        )
    }
}

@Preview
@Composable
fun UserDetailScreenPreview() {
    TalangragaTheme {
        UserDetailScreen(
            userId = 1,
            onBackClick = {}
        )
    }
}
