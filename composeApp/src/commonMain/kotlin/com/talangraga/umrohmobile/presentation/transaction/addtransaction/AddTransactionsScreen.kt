package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.shared.Background
import com.talangraga.shared.BorderColor
import com.talangraga.shared.INDONESIA_TRIMMED
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.formatDateRange
import com.talangraga.shared.toIndonesianDateFormat
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.rememberSharedFileReader
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.CurrencyInputText
import com.talangraga.umrohmobile.ui.component.ModalImagePicker
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.section.PaymentsSheet
import com.talangraga.umrohmobile.ui.section.PeriodsSheet
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import com.talangraga.umrohmobile.ui.utils.formatCurrency
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.tambah_transaksi
import kotlin.time.ExperimentalTime

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */

@Composable
fun AddTransactionScreen(
    navController: NavController,
    isCollective: Boolean = false,
    viewModel: AddTransactionViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddTransactionsContent(
        onBackClick = { navController.popBackStack() },
        userList = uiState.users,
        periods = uiState.periods,
        payments = uiState.payments,
        onPeriodChange = { viewModel.onEvent(AddTransactionEvent.SetSelectedPeriod(it)) },
        selectedPeriod = uiState.selectedPeriod,
        onPaymentChange = { viewModel.onEvent(AddTransactionEvent.SetSelectedPayment(it)) },
        selectedPayment = uiState.selectedPayment,
        isCollective = uiState.isCollective,
        onIsCollectiveChange = { viewModel.onEvent(AddTransactionEvent.SetIsCollective(it)) },
        collectiveMembers = uiState.collectiveMembers,
        onAddMember = { user, amount -> viewModel.onEvent(AddTransactionEvent.AddCollectiveMember(user, amount)) },
        onRemoveMember = { viewModel.onEvent(AddTransactionEvent.RemoveCollectiveMember(it)) },
        imageUri = uiState.imageUri,
        isLoading = uiState.isLoading,
        onImageChange = { viewModel.onEvent(AddTransactionEvent.SetImageUri(it)) },
        onUserChange = { viewModel.onEvent(AddTransactionEvent.SetSelectedUser(it)) },
        selectedUser = uiState.selectedUser,
        onSubmit = { amount, dateMillis, time, user ->
            viewModel.onEvent(AddTransactionEvent.SubmitTransaction(amount, dateMillis, time, user))
        }
    )

    LaunchedEffect(isCollective) {
        viewModel.onEvent(AddTransactionEvent.SetIsCollective(isCollective))
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddTransactionEffect.ShowToastSuccess -> ToastManager.show(
                    message = effect.message,
                    type = ToastType.Success
                )

                is AddTransactionEffect.ShowToastError -> ToastManager.show(
                    message = effect.message,
                    type = ToastType.Error
                )

                AddTransactionEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddTransactionsContent(
    onBackClick: () -> Unit,
    userList: List<UserUIData> = emptyList(),
    periods: List<PeriodEntity>,
    payments: List<PaymentEntity>,
    selectedPeriod: PeriodEntity?,
    onPeriodChange: (PeriodEntity?) -> Unit,
    selectedPayment: PaymentEntity?,
    onPaymentChange: (PaymentEntity?) -> Unit,
    isCollective: Boolean = false,
    onIsCollectiveChange: (Boolean) -> Unit = {},
    collectiveMembers: List<CollectiveMember> = emptyList(),
    onAddMember: (UserUIData, String) -> Unit = { _, _ -> },
    onRemoveMember: (UserUIData) -> Unit = {},
    imageUri: ByteArray? = null,
    isLoading: Boolean = false,
    onImageChange: (ByteArray) -> Unit = {},
    onUserChange: (UserUIData?) -> Unit = {},
    selectedUser: UserUIData? = null,
    onSubmit: (amount: String, dateMillis: Long?, time: String, user: UserUIData?) -> Unit = { _, _, _, _ -> }
) {
    val focusManager = LocalFocusManager.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showUserBottomSheet by remember { mutableStateOf(false) }
    var showPeriodBottomSheet by remember { mutableStateOf(false) }
    var showPaymentBottomSheet by remember { mutableStateOf(false) }

    // State for Add Member Transaction Modal
    var showAddMemberSheet by remember { mutableStateOf(false) }
    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var tempDateMillis by remember { mutableStateOf<Long?>(null) }
    var amount by remember { mutableStateOf("") }

    // Collective Mode State
    var tempMemberUser by remember { mutableStateOf<UserUIData?>(null) }
    var tempMemberAmount by remember { mutableStateOf("") }
    var isUserSelectionForCollective by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val addMemberSheetState = rememberModalBottomSheetState()
    val paymentSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val fileReader = rememberSharedFileReader()
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
                    onImageChange(bytes)
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
            })
    }

    // Logic for form validation
    val isFormValid = remember(
        isCollective,
        collectiveMembers,
        selectedUser,
        amount,
        selectedPeriod,
        selectedPayment,
        tempDateMillis,
        selectedTime,
        imageUri,
        isLoading
    ) {
        val baseValid = selectedPeriod != null &&
                selectedPayment != null &&
                tempDateMillis != null &&
                selectedTime.isNotEmpty() &&
                imageUri != null &&
                !isLoading

        if (isCollective) {
            baseValid && collectiveMembers.isNotEmpty()
        } else {
            baseValid && selectedUser != null && amount.isNotEmpty()
        }
    }

    TalangragaScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tambah Tabungan",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background)
                    .padding(16.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid,
                    onClick = {
                        onSubmit(amount, tempDateMillis, selectedTime, selectedUser)
                    },
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            stringResource(Res.string.tambah_transaksi),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        },
        containerColor = Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    // Upload Bukti Transfer
                    Text(
                        text = "Bukti Transfer",
                        style = TalangragaTypography.titleSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { showImagePickerSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
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

                        if (imageUri != null) {
                            BasicImage(
                                model = imageUri,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        showImagePickerSheet = true
                                    }
                            )
                        } else {
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
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onIsCollectiveChange(!isCollective) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isCollective,
                        onCheckedChange = { onIsCollectiveChange(it) },
                        colors = CheckboxDefaults.colors(checkedColor = Sage)
                    )
                    Text(
                        text = "Tambah Transaksi Kolektif (Beberapa Anggota)",
                        style = TalangragaTypography.bodyMedium
                    )
                }
            }

            item {
                if (!isCollective) {
                    Column {
                        // Anggota/Pengguna Dropdown
                        Text(
                            text = "Anggota/Pengguna",
                            style = TalangragaTypography.titleSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextButtonOption(
                            text = selectedUser?.fullname ?: "",
                            placeholder = "Pilih Anggota",
                            trailingIcon = Icons.Default.ArrowDropDown,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                isUserSelectionForCollective = false
                                showUserBottomSheet = true
                            }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Tabungan ke:",
                    style = TalangragaTypography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val bulan = if (selectedPeriod != null) {
                    formatDateRange(
                        startDateString = selectedPeriod.startDate,
                        endDateString = selectedPeriod.endDate,
                        monthFormat = INDONESIA_TRIMMED
                    )
                } else ""
                TextButtonOption(
                    text = if (selectedPeriod != null) "${selectedPeriod.periodeName}: $bulan" else "Pilih Bulan",
                    placeholder = "Pilih Bulan",
                    trailingIcon = Icons.Default.ArrowDropDown,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    showPeriodBottomSheet = true
                }
            }

            item {
                Column {
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
                        onClick = { showPaymentBottomSheet = true }
                    )
                }
            }

            item {
                Column {
                    // Tanggal & Waktu Picker
                    Text(
                        text = "Tanggal & Waktu",
                        style = TalangragaTypography.titleSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val displayDateTime =
                        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                            "$selectedDate $selectedTime"
                        } else ""

                    TextButtonOption(
                        text = displayDateTime,
                        placeholder = "dd/mm/yyyy HH:mm",
                        trailingIcon = Icons.Default.CalendarToday,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showDatePicker = true }
                    )
                }
            }

            item {
                if (!isCollective) {
                    CurrencyInputText(
                        title = "Jumlah Tabungan",
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = "Rp xxx.xxx.xxx",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                if (isCollective) {
                    Column {
                        Text(
                            text = "Tambahkan Anggota",
                            style = TalangragaTypography.titleSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextButtonOption(
                            text = "",
                            placeholder = "Tambahkan anggota",
                            trailingIcon = Icons.Default.ArrowDropDown,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                // Reset temp fields
                                tempMemberUser = null
                                tempMemberAmount = ""
                                showAddMemberSheet = true
                            }
                        )
                    }
                }
            }

            if (isCollective && collectiveMembers.isNotEmpty()) {
                val totalAmount = collectiveMembers.sumOf { it.amount.toLongOrNull() ?: 0L }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Sage.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, Sage)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Tabungan",
                                style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = totalAmount.toDouble().formatCurrency(),
                                style = TalangragaTypography.titleMedium.copy(
                                    color = Sage,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Daftar anggota",
                        style = TalangragaTypography.titleSmall,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                }

                items(collectiveMembers) { member ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            BorderColor
                        ),
                        colors = CardDefaults.cardColors(containerColor = Background),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = member.user.fullname,
                                style = TalangragaTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = (member.amount.toDoubleOrNull() ?: 0.0).formatCurrency(),
                                style = TalangragaTypography.bodyMedium.copy(
                                    color = Sage,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            tempDateMillis = millis
                            val instant = kotlin.time.Instant.fromEpochMilliseconds(millis)
                            val date = instant.toLocalDateTime(TimeZone.UTC).date
                            selectedDate = date.toIndonesianDateFormat()
                        }
                        showDatePicker = false
                        showTimePicker = true
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val hour = timePickerState.hour.toString().padStart(2, '0')
                        val minute = timePickerState.minute.toString().padStart(2, '0')
                        selectedTime = "$hour:$minute"
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TimeInput(state = timePickerState)
                }
            }
        )
    }

    if (showPeriodBottomSheet) {
        PeriodsSheet(
            modifier = Modifier,
            sheetState = periodSheetState,
            scope = periodScope,
            periods = periods,
            onBottomSheetChange = { showPeriodBottomSheet = it },
            onChoosePeriod = {
                onPeriodChange(it)
            }
        )
    }

    if (showPaymentBottomSheet) {
        PaymentsSheet(
            modifier = Modifier,
            sheetState = paymentSheetState,
            scope = scope,
            payments = payments,
            onBottomSheetChange = { showPaymentBottomSheet = it },
            onChoosePayment = {
                onPaymentChange(it)
            }
        )
    }

    // Add Member Transaction Modal
    if (showAddMemberSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddMemberSheet = false },
            sheetState = addMemberSheetState,
            containerColor = Background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Tambahkan Transaksi Anggota",
                    style = TalangragaTypography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextButtonOption(
                    text = tempMemberUser?.fullname ?: "",
                    placeholder = "Pilih Anggota",
                    trailingIcon = Icons.Default.ArrowDropDown,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isUserSelectionForCollective = true
                        showUserBottomSheet = true
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CurrencyInputText(
                    title = "Jumlah",
                    value = tempMemberAmount,
                    onValueChange = { tempMemberAmount = it },
                    placeholder = "Masukkan jumlah setoran",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = tempMemberUser != null && tempMemberAmount.isNotEmpty(),
                    onClick = {
                        tempMemberUser?.let { user ->
                            onAddMember(user, tempMemberAmount)
                            scope.launch { addMemberSheetState.hide() }.invokeOnCompletion {
                                if (!addMemberSheetState.isVisible) {
                                    showAddMemberSheet = false
                                }
                            }
                        }
                    }
                ) {
                    Text("Tambahkan Transaksi")
                }
            }
        }
    }

    // User Selection Modal
    if (showUserBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showUserBottomSheet = false },
            sheetState = sheetState,
            containerColor = Background
        ) {
            UserSelectionContent(
                userList = userList,
                onUserSelected = { user ->
                    if (isUserSelectionForCollective) {
                        tempMemberUser = user
                    } else {
                        onUserChange(user)
                    }
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showUserBottomSheet = false
                        }
                    }
                }
            )
        }
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

@Composable
fun UserSelectionContent(
    userList: List<UserUIData>,
    onUserSelected: (UserUIData) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredUsers = userList.filter {
        it.fullname.contains(searchQuery, ignoreCase = true) || it.username.contains(
            searchQuery,
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Username") },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Sage, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredUsers) { user ->
                UserItem(user = user, onClick = { onUserSelected(user) })
            }
        }
    }
}

@Composable
fun UserItem(
    user: UserUIData,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Background),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (user.imageProfileUrl.isNotBlank()) {
                    BasicImage(
                        model = user.imageProfileUrl,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Avatar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullname,
                    style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = user.phone,
                    style = TalangragaTypography.bodySmall.copy(color = Color.Gray)
                )
            }

            if (user.userType == "admin") {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF0E0D0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "admin",
                        style = TalangragaTypography.bodySmall.copy(color = Color(0xFF8B4513))
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewAddTransactionContent() {
    val mockUsers = listOf(
        UserUIData(
            id = 1, fullname = "Iqbal Fauzi", phone = "087822882668", userType = "admin",
            username = "iqbalfauzi",
            email = "work.iqbalfauzi@gmail.com",
            domicile = "Bandung",
            imageProfileUrl = "",
            isActive = true
        ),
        UserUIData(
            id = 2, fullname = "Jane Doe", phone = "081234567890", userType = "member",
            username = "janedoe",
            email = "jandoe@gmail.com",
            domicile = "Texas",
            imageProfileUrl = "",
            isActive = true
        )
    )
    TalangragaTheme {
        AddTransactionsContent(
            onBackClick = {},
            userList = mockUsers,
            isCollective = false,
            periods = emptyList(),
            payments = emptyList(),
            onPeriodChange = { },
            selectedPeriod = null,
            onPaymentChange = { },
            selectedPayment = null
        )
    }
}
