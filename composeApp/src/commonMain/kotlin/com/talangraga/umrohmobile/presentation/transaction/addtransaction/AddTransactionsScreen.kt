@file:OptIn(ExperimentalMaterial3Api::class) @file:Suppress("AssignedValueIsNeverRead")

package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.talangraga.shared.Background
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.toIndonesianDateFormat
import com.talangraga.umrohmobile.presentation.transaction.addtransaction.section.PaymentSelectionSection
import com.talangraga.umrohmobile.presentation.transaction.addtransaction.section.UserSelectionSection
import com.talangraga.umrohmobile.presentation.transaction.model.PaymentGroupUIData
import com.talangraga.umrohmobile.presentation.transaction.model.PaymentUIData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.rememberSharedFileReader
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.CurrencyInputText
import com.talangraga.umrohmobile.ui.component.ImageViewerManager
import com.talangraga.umrohmobile.ui.component.LoadingButton
import com.talangraga.umrohmobile.ui.component.ModalImagePicker
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import io.github.aakira.napier.Napier
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.sending_data
import talangragaumrohmobile.composeapp.generated.resources.tambah_transaksi
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
data class CollectiveMember(
    val user: UserUIData, val amount: String
)

@Composable
fun AddTransactionScreen(
    navController: NavController,
    isCollective: Boolean = false,
    viewModel: AddTransactionViewModel = koinViewModel()
) {

    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()
    val payments by viewModel.paymentGroup.collectAsStateWithLifecycle()

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            ToastManager.show(message = errorMessage.orEmpty(), type = ToastType.Error)
        }
    }

    AddTransactionsContent(
        onBackClick = { navController.popBackStack() },
        userList = users,
        paymentList = payments,
        isCollective = isCollective,
        isLoading = false,
        imageUri = viewModel.imageUri.value,
        onImageChange = viewModel::onImageChange,
        user = viewModel.user.value,
        onUserChange = viewModel::onUserChange,
        tabungan = viewModel.tabungan.value,
        onChangeTabungan = viewModel::onTabunganChange,
        date = viewModel.date.value,
        onDateChange = viewModel::onDateChange,
        time = viewModel.time.value,
        onTimeChange = viewModel::onTimeChange,
        selectedPayment = viewModel.payment.value,
        onPaymentChange = viewModel::onPaymentChange,
        addTransaction = viewModel::addTransaction
    )
}

@Suppress("VariableNeverRead")
@OptIn(ExperimentalTime::class)
@Composable
fun AddTransactionsContent(
    onBackClick: () -> Unit,
    userList: List<UserUIData>,
    paymentList: List<PaymentGroupUIData>,
    isLoading: Boolean = false,
    isCollective: Boolean = false,
    imageUri: ByteArray?,
    onImageChange: (ByteArray) -> Unit,
    user: UserUIData? = null,
    onUserChange: (UserUIData) -> Unit,
    tabungan: String?,
    onChangeTabungan: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    time: String,
    onTimeChange: (String) -> Unit,
    selectedPayment: PaymentUIData? = null,
    onPaymentChange: (PaymentUIData) -> Unit,
    addTransaction: () -> Unit
) {

    val fileReader = rememberSharedFileReader() // See helper below
    var showImagePickerSheet by remember { mutableStateOf(false) }
    val imagePickerSheetState = rememberModalBottomSheetState()

    var imagePickerMessage by remember { mutableStateOf<String?>(null) }
    var showGallery by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }

    var capturedPhoto by remember { mutableStateOf<PhotoResult?>(null) }
    var selectedImagesFile by remember { mutableStateOf<List<GalleryPhotoResult>>(emptyList()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showUserBottomSheet by remember { mutableStateOf(false) }
    var showPaymentBottomSheet by remember { mutableStateOf(false) }

    // State for Add Member Transaction Modal
    var showAddMemberSheet by remember { mutableStateOf(false) }

    var tempDateMillis by remember { mutableStateOf<Long?>(null) }

    val sheetState = rememberModalBottomSheetState()
    val paymentSheetState = rememberModalBottomSheetState()
    val addMemberSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(date, time) {
        Napier.i(tag = "KMP", message = "Date: $date, Time: $time")
    }

    LaunchedEffect(capturedPhoto) {
        if (capturedPhoto != null) {
            scope.launch {
                val bytes = fileReader.readBytes(capturedPhoto?.uri.orEmpty())
                if (bytes != null) {
                    onImageChange(bytes)
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
                    onImageChange(bytes)
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
                    Text(text = "Tambah Transaksi", style = TalangragaTypography.titleLarge)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
                modifier = Modifier.fillMaxWidth(),
            )
        }, bottomBar = {

            LoadingButton(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                isLoading = isLoading,
                text = if (isLoading) stringResource(Res.string.sending_data) else stringResource(
                    Res.string.tambah_transaksi
                ),
                enabled = imageUri != null && user != null && !tabungan.isNullOrBlank() && selectedPayment != null && date.isNotBlank() && time.isNotBlank(),
                onClick = {
                    if (!isLoading) {
                        addTransaction()
                    }
                }
            )
        }, containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp).verticalScroll(rememberScrollState()),
        ) {

            // Upload Bukti Transfer
            Text(
                text = "Bukti Transfer",
                style = TalangragaTypography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp))
                    .background(Color.White).clickable { showImagePickerSheet = true },
                contentAlignment = Alignment.Center
            ) {

                if (imageUri != null) {
                    BasicImage(
                        model = imageUri,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clickable {
                            ImageViewerManager.show(imageUri)
                        })
                } else {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = Stroke(
                            width = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                        drawRoundRect(
                            color = Color.LightGray,
                            style = stroke,
                            cornerRadius = CornerRadius(12.dp.toPx())
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Upload",
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tekan disini untuk unggah file",
                            style = TalangragaTypography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Anggota/Pengguna Dropdown
            Text(
                text = "Anggota/Pengguna",
                style = TalangragaTypography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextButtonOption(
                text = user?.fullname ?: "",
                placeholder = "Pilih Anggota",
                trailingIcon = Icons.Default.ArrowDropDown,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    showUserBottomSheet = true
                })

            Spacer(modifier = Modifier.height(16.dp))

            // Jumlah Tabungan Input
            CurrencyInputText(
                title = "Jumlah Tabungan",
                value = tabungan ?: "",
                onValueChange = onChangeTabungan,
                placeholder = "Rp xxx.xxx.xxx",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Metode Pembayaran Dropdown
            Text(
                text = "Metode Pembayaran",
                style = TalangragaTypography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextButtonOption(
                text = selectedPayment?.paymentName ?: "",
                placeholder = "Pilih metode pembayaran",
                trailingIcon = Icons.Default.ArrowDropDown,
                modifier = Modifier.fillMaxWidth(),
                onClick = { showPaymentBottomSheet = true })

            Spacer(modifier = Modifier.height(16.dp))

            // Tanggal & Waktu Picker
            Text(
                text = "Tanggal & Waktu",
                style = TalangragaTypography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextButtonOption(
                text = "$date $time",
                placeholder = "dd/mm/yyyy HH:mm",
                trailingIcon = Icons.Default.CalendarToday,
                modifier = Modifier.fillMaxWidth(),
                onClick = { showDatePicker = true })

            // Collective Section
//            if (isCollective) {
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "Tambahkan Anggota",
//                    style = TalangragaTypography.titleSmall,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//                TextButtonOption(
//                    text = "",
//                    placeholder = "Tambahkan anggota",
//                    trailingIcon = Icons.Default.ArrowDropDown,
//                    modifier = Modifier.fillMaxWidth(),
//                    onClick = {
//                        // Reset temp fields
//                        tempMemberUser = null
//                        tempMemberAmount = ""
//                        showAddMemberSheet = true
//                    }
//                )
//
//                if (collectiveMembers.isNotEmpty()) {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = "Daftar anggota",
//                        style = TalangragaTypography.titleSmall,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        collectiveMembers.forEach { member ->
//                            Card(
//                                shape = RoundedCornerShape(8.dp),
//                                border = BorderStroke(
//                                    1.dp,
//                                    BorderColor
//                                ),
//                                colors = CardDefaults.cardColors(containerColor = Background),
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Row(
//                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Text(
//                                        text = member.user.fullname ?: "Unknown",
//                                        style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
//                                    )
//                                    Text(
//                                        text = member.amount,
//                                        style = TalangragaTypography.bodyMedium.copy(
//                                            color = Sage,
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            tempDateMillis = millis
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val date = instant.toLocalDateTime(TimeZone.UTC).date
                            onDateChange(date.toIndonesianDateFormat())
                        }
                        showDatePicker = false
                        showTimePicker = true
                    }) {
                    Text("OK")
                }
            }, dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
            val timePickerState = rememberTimePickerState()
            AlertDialog(onDismissRequest = { showTimePicker = false }, confirmButton = {
                TextButton(
                    onClick = {
                        val hour = timePickerState.hour.toString().padStart(2, '0')
                        val minute = timePickerState.minute.toString().padStart(2, '0')
                        onTimeChange("$hour:$minute")
                        showTimePicker = false
                    }) {
                    Text("OK")
                }
            }, dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            }, text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TimeInput(state = timePickerState)
                }
            })
        }

        // Add Member Transaction Modal
        if (showAddMemberSheet) {
//            ModalBottomSheet(
//                onDismissRequest = { showAddMemberSheet = false },
//                sheetState = addMemberSheetState,
//                containerColor = Background
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 24.dp)
//                ) {
//                    Text(
//                        text = "Tambahkan Transaksi Anggota",
//                        style = TalangragaTypography.titleLarge,
//                        modifier = Modifier.padding(bottom = 16.dp)
//                    )
//
//                    TextButtonOption(
//                        text = tempMemberUser?.fullname ?: "",
//                        placeholder = "Pilih Anggota",
//                        trailingIcon = Icons.Default.ArrowDropDown,
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = {
//                            isUserSelectionForCollective = true
//                            showUserBottomSheet = true
//                        }
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    CurrencyInputText(
//                        title = "Jumlah",
//                        value = tempMemberAmount,
//                        onValueChange = { tempMemberAmount = it },
//                        placeholder = "Masukkan jumlah setoran",
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    Spacer(modifier = Modifier.height(24.dp))
//
//                    Button(
//                        modifier = Modifier.fillMaxWidth(),
//                        enabled = tempMemberUser != null && tempMemberAmount.isNotEmpty(),
//                        onClick = {
//                            tempMemberUser?.let { user ->
//                                collectiveMembers.add(CollectiveMember(user, tempMemberAmount))
//                                scope.launch { addMemberSheetState.hide() }.invokeOnCompletion {
//                                    if (!addMemberSheetState.isVisible) {
//                                        showAddMemberSheet = false
//                                    }
//                                }
//                            }
//                        }
//                    ) {
//                        Text("Tambahkan Transaksi")
//                    }
//                }
//            }
        }

        // User Selection Modal
        if (showUserBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showUserBottomSheet = false },
                sheetState = sheetState,
                containerColor = Background
            ) {
                UserSelectionSection(
                    userList = userList, onUserSelected = { selected ->
                        onUserChange(selected)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showUserBottomSheet = false
                            }
                        }
                    })
            }
        }

        // Payment Selection Modal
        if (showPaymentBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showPaymentBottomSheet = false },
                sheetState = paymentSheetState,
                containerColor = Background
            ) {
                PaymentSelectionSection(
                    paymentGroups = paymentList,
                    onPaymentSelected = { payment ->
                        onPaymentChange(payment)
                        scope.launch { paymentSheetState.hide() }.invokeOnCompletion {
                            if (!paymentSheetState.isVisible) {
                                showPaymentBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddTransactionContent() {
    val mockUsers = listOf(
        UserUIData(
            id = 1,
            fullname = "Iqbal Fauzi",
            phone = "087822882668",
            userType = "admin",
            username = "iqbalfauzi",
            email = "work.iqbalfauzi@gmail.com",
            domicile = "Bandung",
            imageProfileUrl = "",
            isActive = true
        ), UserUIData(
            id = 2,
            fullname = "Jane Doe",
            phone = "081234567890",
            userType = "member",
            username = "janedoe",
            email = "jandoe@gmail.com",
            domicile = "Texas",
            imageProfileUrl = "",
            isActive = true
        )
    )
    AddTransactionsContent(
        onBackClick = {},
        userList = mockUsers,
        paymentList = emptyList(),
        isCollective = true,
        isLoading = false,
        imageUri = null,
        onImageChange = { },
        user = null,
        onUserChange = { },
        tabungan = "",
        onChangeTabungan = { },
        date = "",
        onDateChange = { },
        time = "",
        onTimeChange = { },
        onPaymentChange = { }
    ) {

    }
}
