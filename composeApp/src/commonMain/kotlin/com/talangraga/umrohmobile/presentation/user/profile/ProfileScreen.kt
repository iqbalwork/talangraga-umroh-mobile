@file:Suppress("AssignedValueIsNeverRead")

package com.talangraga.umrohmobile.presentation.user.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.presentation.home.section.LogoutDialog
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.IconBlock
import com.talangraga.umrohmobile.ui.component.ImageViewerManager
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import com.talangraga.umrohmobile.ui.theme.ThemeManager
import com.talangraga.umrohmobile.ui.theme.ThemeMode
import com.talangraga.umrohmobile.ui.theme.isDynamicColorSupported
import com.talangraga.umrohmobile.ui.utils.isWideScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.logout

@Composable
fun ProfileScreen(
    rootNavHostController: NavHostController,
    navHostController: NavHostController,
    isLoginUser: Boolean,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val profile by viewModel.session.userProfile.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ProfileEvent.FetchProfile)
    }

    val themeManager: ThemeManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()
    val systemDark = isSystemInDarkTheme()

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    val userUiData = profile?.toUiData()
    val displayImageUrl = userUiData?.imageProfileUrl?.ifBlank { null } ?: uiState.imageUrl

    ProfileContent(
        isDarkMode = isDarkTheme,
        isLoginUser = isLoginUser,
        user = userUiData,
        imageUrl = displayImageUrl,
        themeManager = themeManager,
        onLogout = {
            viewModel.onEvent(ProfileEvent.ClearSession)
            rootNavHostController.navigate(Screen.LoginRoute) {
                popUpTo(Screen.MainRoute.ROUTE) {
                    inclusive = true
                }
            }
        },
        onClickEdit = {
            navHostController.navigate(
                Screen.AddUserRoute(
                    userId = profile?.id ?: 0,
                    isEdit = true,
                    isLoginUser = true
                )
            )
        },
        onChangePassword = {
            navHostController.navigate(Screen.ChangePasswordRoute(profile?.id ?: 0))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    isDarkMode: Boolean,
    isLoginUser: Boolean,
    user: UserUIData?,
    imageUrl: String?,
    themeManager: ThemeManager?,
    onClickEdit: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit,
) {
    val isWide = isWideScreen()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }

    TalangragaScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val title = if (isLoginUser) "Profil Saya" else "Profil Pengguna"
                    Text(
                        text = title,
                        style = TalangragaTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 760.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = if (isWide) 24.dp else 120.dp
                )
            ) {
                item {
                    // Profile Header
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!imageUrl.isNullOrBlank()) {
                                BasicImage(
                                    model = imageUrl,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .clickable { ImageViewerManager.show(imageUrl) }
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Avatar",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = user?.fullname.orEmpty(),
                            style = TalangragaTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "@${user?.username.orEmpty()}",
                            style = TalangragaTypography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                item {
                    // User details card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            UserMenuItem(icon = Icons.Filled.Phone, text = user?.phone.orEmpty().ifBlank { "-" })
                            UserMenuItem(icon = Icons.Filled.Email, text = user?.email.orEmpty().ifBlank { "-" })
                            UserMenuItem(icon = Icons.Filled.Place, text = user?.domicile.orEmpty().ifBlank { "-" })
                        }
                    }
                }

                item {
                    // Settings card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Pengaturan",
                                style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            UserMenuItem(
                                icon = Icons.Default.Edit,
                                text = "Ubah Profil",
                                showArrow = true,
                                modifier = Modifier.clickable(onClick = onClickEdit)
                            )

                            UserMenuItem(
                                icon = Icons.Default.Password,
                                text = "Ganti Kata Sandi",
                                showArrow = true,
                                modifier = Modifier.clickable(onClick = onChangePassword)
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                            )

                            themeManager?.let {
                                ThemeToggleScreen(
                                    themeManager = it,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                item {
                    // Logout button
                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = stringResource(Res.string.logout),
                                style = TalangragaTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserMenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    showArrow: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = text,
                style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ThemeToggleScreen(modifier: Modifier = Modifier, themeManager: ThemeManager) {
    val themeMode by themeManager.themeMode.collectAsState()
    val isDynamicColor by themeManager.isDynamicColor.collectAsState()
    val dynamicSupported = remember { isDynamicColorSupported() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Pilihan Tema",
            style = TalangragaTypography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeMode.entries.forEach { mode ->
                val label = when (mode) {
                    ThemeMode.SYSTEM -> "Sistem"
                    ThemeMode.LIGHT -> "Terang"
                    ThemeMode.DARK -> "Gelap"
                }
                val selected = themeMode == mode
                FilterChip(
                    selected = selected,
                    onClick = { themeManager.setTheme(mode) },
                    label = { Text(label, style = TalangragaTypography.bodyMedium) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = "Material You (Dynamic Color)",
                    style = TalangragaTypography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (dynamicSupported) {
                        "Warna tema menyesuaikan wallpaper sistem"
                    } else {
                        "Hanya didukung pada perangkat Android 12+"
                    },
                    style = TalangragaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = isDynamicColor && dynamicSupported,
                onCheckedChange = { enabled ->
                    if (dynamicSupported) {
                        themeManager.setDynamicColor(enabled)
                    }
                },
                enabled = dynamicSupported
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewProfileContent() {
    TalangragaTheme(useDynamicColor = false) {
        ProfileContent(
            user = UserUIData(
                1, "iqbalfauzi", "Iqbal Fauzi", "work.iqbalfauzi@gmail.com", "087822882668",
                domicile = "Bandung",
                userType = "admin",
                imageProfileUrl = "",
                isActive = true
            ),
            isLoginUser = true,
            isDarkMode = false,
            themeManager = null,
            onLogout = {},
            imageUrl = "",
            onClickEdit = { },
            onChangePassword = {}
        )
    }
}
