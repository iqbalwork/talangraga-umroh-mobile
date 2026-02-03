@file:OptIn(ExperimentalMaterial3Api::class)

package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.talangraga.shared.Background
import com.talangraga.shared.BorderColor
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.toIndonesianDateFormat
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.ui.component.CurrencyInputText
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.tambah_transaksi
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */

data class CollectiveMember(
    val user: UserUIData,
    val amount: String
)

@Composable
fun AddTransactionScreen(
    navController: NavController,
    isCollective: Boolean = false,
    viewModel: AddTransactionViewModel = koinViewModel()
) {

    val users by viewModel.users.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    AddTransactionsContent(
        onBackClick = { navController.popBackStack() },
        userList = users,
        isCollective = isCollective
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun AddTransactionsContent(
    onBackClick: () -> Unit,
    userList: List<UserUIData> = emptyList(),
    isCollective: Boolean = false
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showUserBottomSheet by remember { mutableStateOf(false) }

    // State for Add Member Transaction Modal
    var showAddMemberSheet by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var tempDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedUser by remember { mutableStateOf<UserUIData?>(null) }

    // Collective Mode State
    val collectiveMembers = remember { mutableStateListOf<CollectiveMember>() }
    var tempMemberUser by remember { mutableStateOf<UserUIData?>(null) }
    var tempMemberAmount by remember { mutableStateOf("") }
    var isUserSelectionForCollective by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val addMemberSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
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
                    onClick = { /* Handle submit */ },
                ) {
                    Text(
                        stringResource(Res.string.tambah_transaksi),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {

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
                    .clickable { /* Handle upload click */ },
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

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            // Jumlah Tabungan Input
            var amount by remember { mutableStateOf("") }
            CurrencyInputText(
                title = "Jumlah Tabungan",
                value = amount,
                onValueChange = { amount = it },
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
                text = "",
                placeholder = "Pilih metode pembayaran",
                trailingIcon = Icons.Default.ArrowDropDown,
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* Show payment method dropdown */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tanggal & Waktu Picker
            Text(
                text = "Tanggal & Waktu",
                style = TalangragaTypography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val displayDateTime = if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                "$selectedDate $selectedTime"
            } else ""

            TextButtonOption(
                text = displayDateTime,
                placeholder = "dd/mm/yyyy HH:mm",
                trailingIcon = Icons.Default.CalendarToday,
                modifier = Modifier.fillMaxWidth(),
                onClick = { showDatePicker = true }
            )

            // Collective Section
            if (isCollective) {
                Spacer(modifier = Modifier.height(16.dp))

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

                if (collectiveMembers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Daftar anggota",
                        style = TalangragaTypography.titleSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        collectiveMembers.forEach { member ->
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
                                        text = member.user.fullname ?: "Unknown",
                                        style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = member.amount,
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

            Spacer(modifier = Modifier.height(24.dp))
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
                                val instant = Instant.fromEpochMilliseconds(millis)
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
                                collectiveMembers.add(CollectiveMember(user, tempMemberAmount))
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
                            selectedUser = user
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
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Sage
                )
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
    AddTransactionsContent(
        onBackClick = {},
        userList = mockUsers,
        isCollective = true
    )
}
