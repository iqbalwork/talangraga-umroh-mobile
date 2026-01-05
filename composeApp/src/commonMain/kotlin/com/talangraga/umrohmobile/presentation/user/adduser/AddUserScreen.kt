@file:OptIn(ExperimentalMaterial3Api::class) @file:Suppress("AssignedValueIsNeverRead")

package com.talangraga.umrohmobile.presentation.user.adduser

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.shared.Background
import com.talangraga.shared.Red
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.mandatory
import com.talangraga.umrohmobile.presentation.utils.rememberSharedFileReader
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.InputText
import com.talangraga.umrohmobile.ui.component.InputTextWithStylingTitle
import com.talangraga.umrohmobile.ui.component.LoadingButton
import com.talangraga.umrohmobile.ui.component.ModalImagePicker
import com.talangraga.umrohmobile.ui.component.PasswordInput
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddUserScreen(
    navController: NavHostController, viewModel: AddUserViewModel = koinViewModel()
) {

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isSuccess by viewModel.isSuccess.collectAsStateWithLifecycle()

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            ToastManager.show(message = errorMessage.orEmpty(), type = ToastType.Error)
            viewModel.clearError()
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.popBackStack()
        }
    }

    AddUserContent(
        onBackClick = { navController.popBackStack() },
        fullname = viewModel.fullname.value,
        onFullnameChange = viewModel::onFullnameChange,
        username = viewModel.username.value,
        onUsernameChange = viewModel::onUsernameChange,
        phoneNumber = viewModel.phoneNumber.value,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        email = viewModel.email.value,
        onEmailChange = viewModel::onEmailChange,
        domicile = viewModel.domicile.value,
        onDomicileChange = viewModel::onDomicileChange,
        userType = viewModel.userType.value,
        onUserTypeChange = viewModel::onUserTypeChange,
        password = viewModel.password.value,
        onPasswordChange = viewModel::onPasswordChange,
        confirmPassword = viewModel.confirmPassword.value,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        imageUrl = viewModel.imageUri.value,
        onImageUrlChange = viewModel::onImageChange,
        isLoading = isLoading,
        onSaveClick = viewModel::registerUser,
    )
}

@Composable
fun AddUserContent(
    onBackClick: () -> Unit,
    fullname: String,
    onFullnameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    domicile: String,
    onDomicileChange: (String) -> Unit,
    userType: String,
    onUserTypeChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    imageUrl: ByteArray?,
    onImageUrlChange: (ByteArray) -> Unit,
    isLoading: Boolean,
    onSaveClick: () -> Unit
) {

    val fileReader = rememberSharedFileReader() // See helper below
    val scope = rememberCoroutineScope()

    var buttonHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    var showImagePickerSheet by remember { mutableStateOf(false) }
    val imagePickerSheetState = rememberModalBottomSheetState()

    var imagePickerMessage by remember { mutableStateOf<String?>(null) }
    var showGallery by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }

    var capturedPhoto by remember { mutableStateOf<PhotoResult?>(null) }
    var selectedImagesFile by remember { mutableStateOf<List<GalleryPhotoResult>>(emptyList()) }

    LaunchedEffect(capturedPhoto) {
        if (capturedPhoto != null) {
            scope.launch {
                val bytes = fileReader.readBytes(capturedPhoto?.uri.orEmpty())
                if (bytes != null) {
                    onImageUrlChange(bytes)
                } else {
                    return@launch
                }
            }
        }
    }

    LaunchedEffect(selectedImagesFile) {
        if (selectedImagesFile.isNotEmpty()) {
            scope.launch {
                val bytes = fileReader.readBytes(selectedImagesFile.first().uri)
                if (bytes != null) {
                    onImageUrlChange(bytes)
                } else {
                    return@launch
                }
            }
        }
    }

    LaunchedEffect(imagePickerMessage) {
        if (!imagePickerMessage.isNullOrEmpty()) {
            ToastManager.show(message = imagePickerMessage.orEmpty(), type = ToastType.Error)
            imagePickerMessage = null
        }
    }

    if (showGallery) {
        GalleryPickerLauncher(onPhotosSelected = {
            selectedImagesFile = it
            showCamera = false
            showGallery = false
        }, onError = {
            imagePickerMessage = it.message.orEmpty()
            showCamera = false
            showGallery = false
        })
    }

    if (showImagePickerSheet) {
        ModalImagePicker(
            onDismissRequest = { showImagePickerSheet = false }, onCameraClick = {
                showImagePickerSheet = false
                showCamera = true
                showGallery = false
            }, onGalleryClick = {
                showImagePickerSheet = false
                showCamera = false
                showGallery = true
            }, sheetState = imagePickerSheetState
        )
    }

    TalangragaScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tambah Pengguna Baru", style = TalangragaTypography.titleLarge
                    )
                }, navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }, containerColor = Background
    ) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            val (inputRef, buttonRef) = createRefs()

            LazyColumn(
                modifier = Modifier.constrainAs(inputRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Profile Image Placeholder
                        Box(
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier, contentAlignment = Alignment.Center
                            ) {
                                BasicImage(
                                    model = imageUrl,
                                    modifier = Modifier.size(124.dp).clip(CircleShape)
                                )
                            }

                            Box(
                                modifier = Modifier.align(Alignment.BottomEnd)
                                    .padding(bottom = 8.dp, end = 8.dp).size(32.dp)
                                    .clip(CircleShape).background(Color.White)
                                    .border(1.dp, Color.LightGray, CircleShape).clickable {
                                        showImagePickerSheet = true
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change Photo",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black
                                )
                            }
                        }

//                        Spacer(modifier = Modifier.height(24.dp))

                        // Data Pengguna Section
                        Text(
                            text = "Data Pengguna",
                            style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        InputTextWithStylingTitle(
                            title = "Nama Lengkap".mandatory(),
                            value = fullname,
                            onValueChange = onFullnameChange,
                            placeholder = "Masukkan nama lengkap",
                            keyboardCapitalization = KeyboardCapitalization.Words,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        InputTextWithStylingTitle(
                            title = "Nama Pengguna".mandatory(),
                            value = username,
                            onValueChange = onUsernameChange,
                            placeholder = "Masukkan nama pengguna",
                            keyboardType = KeyboardType.Uri,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        InputText(
                            title = "Nomor Telepon",
                            value = phoneNumber,
                            onValueChange = onPhoneNumberChange,
                            placeholder = "Masukkan nomor telepon",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        val emailLabel = buildAnnotatedString {
                            append("Email")
                            withStyle(style = SpanStyle(color = Red)) {
                                append("*")
                            }
                        }
                        InputTextWithStylingTitle(
                            title = emailLabel,
                            value = email,
                            onValueChange = onEmailChange,
                            placeholder = "Masukkan email",
                            keyboardType = KeyboardType.Email,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        InputTextWithStylingTitle(
                            title = "Domisili".mandatory(),
                            value = domicile,
                            onValueChange = onDomicileChange,
                            placeholder = "Masukkan domisili",
                            keyboardCapitalization = KeyboardCapitalization.Words,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Jenis User".mandatory(),
                            style = TalangragaTypography.titleSmall,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                        )

                        var expanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            TextButtonOption(
                                text = userType,
                                placeholder = "Pilih Jenis User",
                                trailingIcon = Icons.Default.ArrowDropDown,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { expanded = true })
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                DropdownMenuItem(text = { Text("Member") }, onClick = {
                                    onUserTypeChange("Member")
                                    expanded = false
                                })
                                DropdownMenuItem(text = { Text("Admin") }, onClick = {
                                    onUserTypeChange("Admin")
                                    expanded = false
                                })
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Kata Sandi Section
                        Text(
                            text = "Kata Sandi",
                            style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        PasswordInput(
                            title = "Kata sandi baru",
                            password = password,
                            onPasswordChange = onPasswordChange,
                            placeholder = "Masukkan kata sandi baru Anda",
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (password.isNotEmpty() && password.length < 8) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = " Kata sandi minimal 8 karakter",
                                    style = TalangragaTypography.bodySmall.copy(color = Color.Red)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        PasswordInput(
                            title = "Konfirmasi kata sandi baru",
                            password = confirmPassword,
                            onPasswordChange = onConfirmPasswordChange,
                            placeholder = "Konfirmasi kata sandi baru Anda",
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (confirmPassword.isNotEmpty() && confirmPassword.length < 8) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = " Kata sandi minimal 8 karakter",
                                    style = TalangragaTypography.bodySmall.copy(color = Color.Red)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(buttonHeight + 16.dp))
                    }
                }
            }

            val enableButton =
                fullname.isNotBlank() && username.isNotBlank() && email.isNotBlank() && domicile.isNotBlank() && (password.isNotBlank() && confirmPassword.isNotBlank() && (confirmPassword == password))
            LoadingButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        buttonHeight = with(localDensity) { coordinates.size.height.toDp() }
                    }.constrainAs(buttonRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                isLoading = isLoading,
                text = if (isLoading) "Sedang mengirim data..." else "Simpan Perubahan",
                enabled = enableButton,
                onClick = {
                    if (!isLoading) {
                        onSaveClick()
                    }
                }
            )
        }
    }

    Box(Modifier.fillMaxSize()) {
        if (showCamera) {
            ImagePickerLauncher(config = ImagePickerConfig(onPhotoCaptured = {
                capturedPhoto = it
                showCamera = false
                showGallery = false
            }, onError = {
                imagePickerMessage = it.message.orEmpty()
                showCamera = false
                showGallery = false
            }))
        }
    }
}

@Preview
@Composable
fun AddUserScreenPreview() {
    TalangragaTheme {
        AddUserContent(
            onBackClick = {},
            fullname = "",
            onFullnameChange = {},
            username = "",
            onUsernameChange = {},
            phoneNumber = "",
            onPhoneNumberChange = {},
            email = "",
            onEmailChange = {},
            domicile = "",
            onDomicileChange = {},
            userType = "Member",
            onUserTypeChange = {},
            password = "",
            onPasswordChange = {},
            confirmPassword = "",
            onConfirmPasswordChange = {},
            onSaveClick = {},
            imageUrl = null,
            isLoading = false,
            onImageUrlChange = { })
    }
}
