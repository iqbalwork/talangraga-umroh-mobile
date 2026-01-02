@file:Suppress("AssignedValueIsNeverRead")

package com.talangraga.umrohmobile.presentation.user.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.shared.AccentRed
import com.talangraga.shared.Red
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.presentation.home.section.LogoutDialog
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.IconBlock
import com.talangraga.umrohmobile.ui.component.ImageViewerManager
import com.talangraga.umrohmobile.ui.component.ModalImagePicker
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import com.talangraga.umrohmobile.ui.theme.ThemeManager
import com.talangraga.umrohmobile.ui.theme.ThemeMode
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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

    val profile by viewModel.profile.collectAsStateWithLifecycle()

    val themeManager: ThemeManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()
    val systemDark = isSystemInDarkTheme()

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    ProfileContent(
        isDarkMode = isDarkTheme,
        isLoginUser = isLoginUser,
        user = profile,
        imageUrl = viewModel.imageUrl.value,
        onImageUrlChange = viewModel::onImageChange,
        themeManager = themeManager,
        onLogout = {
            viewModel.clearSession()
            rootNavHostController.navigate(Screen.LoginRoute) {
                popUpTo(Screen.MainRoute.ROUTE) {
                    inclusive = true
                }
            }
        },
        onClickEdit = {
            navHostController.navigate(Screen.EditProfileRoute(profile?.id ?: 0, true))
        },
        onChangePassword = {
            navHostController.navigate(Screen.ChangePasswordRoute(profile?.id ?: 0))
        },
        onClickBack = { navHostController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    isDarkMode: Boolean,
    isLoginUser: Boolean,
    user: UserUIData?,
    imageUrl: String?,
    onImageUrlChange: (String) -> Unit,
    themeManager: ThemeManager?,
    onClickEdit: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit,
    onClickBack: () -> Unit
) {

    var showLogoutDialog by remember { mutableStateOf(false) }

    var showImagePickerSheet by remember { mutableStateOf(false) }
    val imagePickerSheetState = rememberModalBottomSheetState()

    var imagePickerMessage by remember { mutableStateOf<String?>(null) }
    var showGallery by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }

    var capturedPhoto by remember { mutableStateOf<PhotoResult?>(null) }
    var selectedImagesFile by remember { mutableStateOf<List<GalleryPhotoResult>>(emptyList()) }

    LaunchedEffect(capturedPhoto) {
        if (capturedPhoto != null) {
            onImageUrlChange(capturedPhoto?.uri.orEmpty())
        }
    }

    LaunchedEffect(selectedImagesFile) {
        if (selectedImagesFile.isNotEmpty()) {
            onImageUrlChange(selectedImagesFile.first().uri)
        }
    }

    LaunchedEffect(imagePickerMessage) {
        if (!imagePickerMessage.isNullOrEmpty()) {
            ToastManager.show(message = imagePickerMessage.orEmpty(), type = ToastType.Error)
            imagePickerMessage = null
        }
    }

    if (showCamera) {
        ImagePickerLauncher(
            config = ImagePickerConfig(
                onPhotoCaptured = {
                    capturedPhoto = it
                    showCamera = false
                    showGallery = false
                },
                onError = {
                    imagePickerMessage = it.message.orEmpty()
                    showCamera = false
                    showGallery = false
                }
            )
        )
    }

    if (showGallery) {
        GalleryPickerLauncher(
            onPhotosSelected = {
                selectedImagesFile = it
                showCamera = false
                showGallery = false
            },
            onError = {
                imagePickerMessage = it.message.orEmpty()
                showCamera = false
                showGallery = false
            }
        )
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }

    if (showImagePickerSheet) {
        ModalImagePicker(
            onDismissRequest = { showImagePickerSheet = false },
            onCameraClick = {
                showImagePickerSheet = false
                showCamera = true
                showGallery = false
            },
            onGalleryClick = {
                showImagePickerSheet = false
                showCamera = false
                showGallery = true
            },
            sheetState = imagePickerSheetState
        )
    }

    TalangragaScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val title = if (isLoginUser) "Profil Saya" else "Profil Pengguna"
                    Text(text = title, style = TalangragaTypography.titleLarge)
                },
                modifier = Modifier,
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (imageProfileRef, fullNameRef, usernameRef) = createRefs()

                    BasicImage(
                        model = imageUrl.orEmpty(),
                        modifier = Modifier
                            .clickable {
                                ImageViewerManager.show(imageUrl)
                            }
                            .size(124.dp)
                            .clip(CircleShape)
                            .constrainAs(imageProfileRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                showImagePickerSheet = true
                            }
                            .clip(CircleShape)
                            .border(width = 1.dp, color = Color.White, shape = CircleShape)
                            .background(color = Color.White)
                            .padding(2.dp)
                            .border(width = 1.dp, color = Color.DarkGray, shape = CircleShape)
                            .padding(4.dp)
                            .constrainAs(createRef()) {
                                end.linkTo(imageProfileRef.end, 4.dp)
                                bottom.linkTo(imageProfileRef.bottom, 4.dp)
                            }
                    )

                    Text(
                        text = user?.fullname.orEmpty(),
                        style = TalangragaTypography.titleLarge,
                        modifier = Modifier.constrainAs(fullNameRef) {
                            top.linkTo(imageProfileRef.bottom, 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )

                    val username = "@${user?.username.orEmpty()}"
                    Text(
                        text = username,
                        modifier = Modifier.constrainAs(usernameRef) {
                            top.linkTo(fullNameRef.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UserMenuItem(icon = Icons.Filled.Phone, text = user?.phone.orEmpty())
                        UserMenuItem(icon = Icons.Filled.Email, text = user?.email.orEmpty())
                        UserMenuItem(icon = Icons.Filled.Place, text = user?.domicile.orEmpty())
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        val (settingLabelRef, settingRef, iconRef, modeRef, switchRef) = createRefs()

                        Text(
                            text = "Pengaturan",
                            style = TalangragaTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.constrainAs(settingLabelRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                        )

                        Column(modifier = Modifier.constrainAs(settingRef) {
                            top.linkTo(settingLabelRef.bottom, 8.dp)
                            start.linkTo(parent.start); end.linkTo(parent.end)
                        }, verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        }

                        IconBlock(
                            icon = Icons.Filled.LightMode,
                            startColor = Sage,
                            endColor = Sage,
                            size = 40.dp,
                            iconSize = 24.dp,
                            modifier = Modifier.constrainAs(iconRef) {
                                top.linkTo(settingRef.bottom, 8.dp)
                                start.linkTo(parent.start)
                            }
                        )
                        val mode = if (isDarkMode) "Gelap" else "Terang"
                        Text(
                            text = "Mode $mode",
                            style = TalangragaTypography.titleLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.constrainAs(modeRef) {
                                top.linkTo(iconRef.top)
                                bottom.linkTo(iconRef.bottom)
                                start.linkTo(iconRef.end, 16.dp)
                            }
                        )

                        themeManager?.let {
                            ThemeToggleScreen(
                                themeManager = it,
                                modifier = Modifier.fillMaxWidth().constrainAs(switchRef) {
                                    start.linkTo(iconRef.start)
                                    top.linkTo(iconRef.bottom, 16.dp)
                                })
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        // Show Logout Dialog
                        showLogoutDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    border = BorderStroke(width = 1.dp, color = Red)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(Res.string.logout),
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Red
                        )
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
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconBlock(
                icon = icon,
                startColor = Sage,
                endColor = Sage,
                size = 40.dp,
                iconSize = 24.dp,
                modifier = Modifier
            )
            Text(
                text = text,
                style = TalangragaTypography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                modifier = Modifier
            )
        }
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ThemeToggleScreen(modifier: Modifier, themeManager: ThemeManager) {
    val themeMode by themeManager.themeMode.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Select Theme:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ThemeMode.entries.forEach { mode ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.clickable { themeManager.setTheme(mode) }
                ) {
                    RadioButton(
                        selected = themeMode == mode,
                        onClick = { themeManager.setTheme(mode) })
                    Text(text = mode.name)
                }
            }
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
            onClickBack = { },
            isLoginUser = true,
            isDarkMode = false,
            themeManager = null,
            onLogout = {},
            imageUrl = "",
            onImageUrlChange = {

            },
            onClickEdit = { },
            onChangePassword = {}
        )
    }
}
