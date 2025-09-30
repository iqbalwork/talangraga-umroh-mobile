package com.talangraga.umrohmobile.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.presentation.home.section.LogoutDialog
import com.talangraga.umrohmobile.presentation.home.section.PeriodSection
import com.talangraga.umrohmobile.presentation.home.section.ProfileSection
import com.talangraga.umrohmobile.presentation.home.section.TransactionSection
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.ui.Aqua
import com.talangraga.umrohmobile.ui.Background
import com.talangraga.umrohmobile.ui.Green
import com.talangraga.umrohmobile.ui.MediumPurple
import com.talangraga.umrohmobile.ui.RosePink
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.section.CardInfoSection
import com.talangraga.umrohmobile.ui.section.DialogPeriods
import com.talangraga.umrohmobile.ui.section.DialogUserType
import com.talangraga.umrohmobile.util.currentDate
import com.talangraga.umrohmobile.util.formatToIDR
import com.talangraga.umrohmobile.util.isDateInRange
import io.github.aakira.napier.Napier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    justLogin: Boolean,
    viewModel: HomeViewModel = koinViewModel()
) {

    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val periods by viewModel.periods.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        user = profile,
        periods = periods,
        transactions = transactions,
        uiState = uiState,
        onFetchProfile = {
            viewModel.getProfile()
        }
    ) {
        viewModel.clearSession()
        navHostController.navigate(LoginRoute) {
            popUpTo(HomeRoute()) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(justLogin) {
        if (justLogin) {
            viewModel.getProfile()
        } else {
            viewModel.getLocalProfile()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    user: UserEntity?,
    periods: List<PeriodEntity>,
    transactions: List<TransactionEntity>,
    uiState: HomeUiState,
    onFetchProfile: () -> Unit,
    onLogout: () -> Unit // This is the actual logout logic
) {

    var userType by remember { mutableStateOf(user?.userType) }
    val userTypeIcon by remember {
        derivedStateOf {
            if (userType == "Admin") Icons.Default.Security else Icons.Default.AccountCircle
        }
    }
    var period by remember { mutableStateOf(periods.firstOrNull()) }
    var showLogoutDialog by remember { mutableStateOf(false) } // State for dialog visibility

    val userTypeSheetState = rememberModalBottomSheetState()
    val userTypeScope = rememberCoroutineScope()
    var userTypeShowBottomSheet by remember { mutableStateOf(false) }
    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var periodShowBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(periods) {
        period = periods.find { data ->
            currentDate.isDateInRange(data.startDate, data.endDate)
        } ?: periods.firstOrNull()
        Napier.i(message = "Selected period: ${period?.periodeName.orEmpty()}")
    }

    LaunchedEffect(user) {
        user?.let {
            userType = it.userType
        }
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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                shape = CircleShape,
                containerColor = Green
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Transaction, User, or Period",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        if (userTypeShowBottomSheet) {
            DialogUserType(
                modifier = Modifier,
                sheetState = userTypeSheetState,
                scope = userTypeScope,
                onBottomSheetChange = {
                    userTypeShowBottomSheet = it
                },
                onChooseUserType = {
                    userType = it
                }
            )
        }

        if (periodShowBottomSheet) {
            DialogPeriods(
                modifier = Modifier,
                sheetState = periodSheetState,
                scope = periodScope,
                periods = periods,
                onBottomSheetChange = {
                    periodShowBottomSheet = it
                },
                onChoosePeriod = {
                    period = it
                }
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column(
                    modifier = Modifier
                        .background(color = Background)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileSection(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(top = paddingValues.calculateTopPadding()),
                        userType = userType,
                        userTypeIcon = userTypeIcon,
                        userTypeShowBottomSheet = userTypeShowBottomSheet,
                        state = uiState.profile,
                        onShowUserTypeSheet = {
                            userTypeShowBottomSheet = true
                        },
                        onRetry = onFetchProfile,
                        onLogout = {
                            showLogoutDialog = true
                        }
                    )
                    PeriodSection(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 16.dp),
                        period = period,
                        onShowPeriodSheet = {
                            periodShowBottomSheet = true
                        },
                    )

                    if (transactions.isNotEmpty()) {
                        val totalAmount = transactions.sumOf { it.amount }
                        CardInfoSection(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            title = "Total Tabungan Periode ini",
                            value = totalAmount.formatToIDR(),
                            notes = "12% dari bulan lalu",
                            notesColor = Green,
                            icon = Icons.Default.AttachMoney,
                            startIconColor = Aqua,
                            endIconColor = Green
                        )
                        val totalMember = transactions.distinctBy { it.reportedBy }.size
                        CardInfoSection(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            title = "Anggota yang Menabung",
                            value = totalMember.toString(),
                            notes = "Bulan ini",
                            notesColor = MediumPurple,
                            icon = Icons.Default.AccountCircle,
                            startIconColor = MediumPurple,
                            endIconColor = MediumPurple
                        )
                        val average = transactions.map { it.amount }.average()
                        CardInfoSection(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            title = "Rata-rata Tabungan",
                            value = average.toInt().formatToIDR(),
                            notes = "Per Anggota/Bulan",
                            notesColor = RosePink,
                            icon = Icons.Default.Calculate,
                            startIconColor = RosePink,
                            endIconColor = RosePink
                        )

                        TransactionSection(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            transactions = transactions
                        ) {
                            
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    TalangragaTheme {
        HomeContent(
            UserEntity(
                userId = 1,
                userName = "iqbalf",
                fullname = "Iqbal Fauzi",
                email = "",
                phone = "",
                domisili = "",
                userType = "Admin",
                imageProfileUrl = ""
            ),
            periods = listOf(
                PeriodEntity("", "Bulan ke 1", "2025-08-06", "2025-09-05"),
            ),
            transactions = listOf(
                TransactionEntity(
                    transactionId = 1,
                    amount = 500000,
                    transactionDate = "2025-08-29T22:15:00.000Z",
                    reportedDate = "2025-08-29T22:15:00.000Z",
                    statusTransaksi = "Confirmed",
                    buktiTransferUrl = "",
                    paymentType = "",
                    paymentName = "",
                    reportedBy = "Iqbal Fauzi",
                    confirmedBy = "Eko Yulianto"
                ),
                TransactionEntity(
                    transactionId = 2,
                    amount = 250000,
                    transactionDate = "2025-08-29T22:15:00.000Z",
                    reportedDate = "2025-08-29T22:15:00.000Z",
                    statusTransaksi = "Confirmed",
                    buktiTransferUrl = "",
                    paymentType = "",
                    paymentName = "",
                    reportedBy = "Iqbal Fauzi",
                    confirmedBy = "Eko Yulianto"
                ),
                TransactionEntity(
                    transactionId = 3,
                    amount = 200000,
                    transactionDate = "2025-08-29T22:15:00.000Z",
                    reportedDate = "2025-08-29T22:15:00.000Z",
                    statusTransaksi = "Confirmed",
                    buktiTransferUrl = "",
                    paymentType = "",
                    paymentName = "",
                    reportedBy = "Iqbal Fauzi",
                    confirmedBy = "Eko Yulianto"
                ),
            ),
            uiState = HomeUiState(
                profile = SectionState.Success(
                    UserEntity(
                        userId = 1,
                        userName = "iqbalf",
                        fullname = "Iqbal Fauzi",
                        email = "",
                        phone = "",
                        domisili = "",
                        userType = "Admin",
                        imageProfileUrl = ""
                    )
                ),
                periods = SectionState.Loading,
                transactions = SectionState.Loading
            ),
            onFetchProfile = {},
            onLogout = {} // Preview doesn't need to do real logout
        )
    }
}
