package com.talangraga.umrohmobile.presentation.user.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.shared.Background
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.InputText
import com.talangraga.umrohmobile.ui.component.ModalImagePicker
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import org.koin.compose.viewmodel.koinViewModel

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun EditProfileScreen(
    navHostController: NavHostController,
    userId: Int,
    isLoginUser: Boolean,
    viewModel: EditProfileViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.onEvent(EditProfileEvent.InitScope(userId, isLoginUser))
    }

    LaunchedEffect(uiState.errorMessage) {
        if (!uiState.errorMessage.isNullOrEmpty()) {
            ToastManager.show(message = uiState.errorMessage.orEmpty(), type = ToastType.Error)
            viewModel.onEvent(EditProfileEvent.ClearError)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navHostController.popBackStack()
        }
    }

    EditProfileContent(
        onBackClick = { navHostController.popBackStack() },
        fullname = uiState.fullname,
        onFullnameChange = { viewModel.onEvent(EditProfileEvent.OnFullnameChange(it)) },
        phoneNumber = uiState.phoneNumber,
        onPhoneNumberChange = { viewModel.onEvent(EditProfileEvent.OnPhoneNumberChange(it)) },
        email = uiState.email,
        onEmailChange = { viewModel.onEvent(EditProfileEvent.OnEmailChange(it)) },
        domicile = uiState.domicile,
        onDomicileChange = { viewModel.onEvent(EditProfileEvent.OnDomicileChange(it)) },
        imageUrl = uiState.imageUrl,
        onImageUrlChange = { viewModel.onEvent(EditProfileEvent.OnImageChange(it)) },
        onSaveClick = { viewModel.onEvent(EditProfileEvent.SaveProfile) },
        isLoading = uiState.isLoading
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    onBackClick: () -> Unit,
    fullname: String,
    onFullnameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    domicile: String,
    onDomicileChange: (String) -> Unit,
    imageUrl: String? = null,
    onImageUrlChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    isLoading: Boolean
) {

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
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ubah Profil",
                        style = TalangragaTypography.titleLarge
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    BasicImage(
                        model = imageUrl.orEmpty(),
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color.LightGray, CircleShape)
                        .clickable { /* Pick Image */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change Photo",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            InputText(
                title = "Nama Lengkap",
                value = fullname,
                onValueChange = onFullnameChange,
                placeholder = "Masukkan nama lengkap",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            InputText(
                title = "Nomor Telepon",
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                placeholder = "Masukkan nomor telepon",
                keyboardType = KeyboardType.Phone,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            InputText(
                title = "Email",
                value = email,
                onValueChange = onEmailChange,
                placeholder = "Masukkan email",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            InputText(
                title = "Domisili",
                value = domicile,
                onValueChange = onDomicileChange,
                placeholder = "Masukkan domisili",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Sage),
                enabled = fullname.isNotBlank() && domicile.isNotBlank()
            ) {
                Text("Simpan Perubahan")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun PreviewEditProfileContent() {
    EditProfileContent(
        onBackClick = { },
        fullname = "",
        onFullnameChange = { },
        phoneNumber = "",
        onPhoneNumberChange = { },
        email = "",
        onEmailChange = { },
        domicile = "",
        onDomicileChange = { },
        imageUrl = null,
        onImageUrlChange = { },
        onSaveClick = { },
        isLoading = false
    )
}
