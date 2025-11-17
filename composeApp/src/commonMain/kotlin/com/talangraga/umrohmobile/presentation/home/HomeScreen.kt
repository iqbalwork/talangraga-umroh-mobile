package com.talangraga.umrohmobile.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.presentation.home.section.LogoutDialog
import com.talangraga.umrohmobile.presentation.home.section.PeriodSection
import com.talangraga.umrohmobile.presentation.home.section.ProfileSection
import com.talangraga.umrohmobile.presentation.home.section.TransactionSection
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.ListUserRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.presentation.navigation.UserRoute
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.ui.Green
import com.talangraga.umrohmobile.ui.component.TextButton
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import com.talangraga.umrohmobile.ui.section.DialogPeriods
import com.talangraga.umrohmobile.ui.section.DialogUserType
import com.talangraga.umrohmobile.util.INDONESIA_TRIMMED
import com.talangraga.umrohmobile.util.formatDateRange
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    justLogin: Boolean,
    viewModel: HomeViewModel = koinViewModel(),
) {

    val periods by viewModel.periods.collectAsStateWithLifecycle()
    val userType by viewModel.userType.collectAsStateWithLifecycle()
    val role by viewModel.role.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getProfileIfNecessary(justLogin)
    }

    HomeContent(
        userType = userType.orEmpty(),
        role = role,
        onUserTypeChange = viewModel::setUserType,
        periods = periods,
        uiState = uiState,
        errorMessage = errorMessage.orEmpty(),
        selectedPeriod = viewModel.selectedPeriod.value,
        onPeriodChange = {
            viewModel.setSelectedPeriod(it)
            viewModel.getTransactions(it.periodId)
        },
        onUserCLick = {
            navHostController.navigate(UserRoute(user = it, isLoginUser = true)) {
                launchSingleTop = true
                restoreState = true
            }
        },
        onListUserClick = {
            navHostController.navigate(ListUserRoute) {
                launchSingleTop = true
                restoreState = true
            }
        },
        onFetchProfile = viewModel::getProfile,
        onSeeMoreTransaction = { },
        onAddTransaction = { },
    ) {
        viewModel.clearSession()
        navHostController.navigate(LoginRoute) {
            popUpTo(HomeRoute()) {
                inclusive = true
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    userType: String,
    role: String,
    onUserTypeChange: (String) -> Unit,
    periods: List<PeriodEntity>,
    uiState: HomeUiState,
    errorMessage: String,
    selectedPeriod: PeriodEntity?,
    onPeriodChange: (PeriodEntity) -> Unit,
    onSeeMoreTransaction: () -> Unit,
    onAddTransaction: () -> Unit,
    onUserCLick: (UserUIData) -> Unit,
    onListUserClick: () -> Unit,
    onFetchProfile: () -> Unit,
    onLogout: () -> Unit
) {

    var showLogoutDialog by remember { mutableStateOf(false) }
    var period by remember { mutableStateOf<PeriodEntity?>(null) }

    val userTypeSheetState = rememberModalBottomSheetState()
    val userTypeScope = rememberCoroutineScope()
    var userTypeShowBottomSheet by remember { mutableStateOf(false) }
    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var periodShowBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        scope.launch {
            if (errorMessage.isNotEmpty()) snackbarHostState.showSnackbar(errorMessage)
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
                onClick = { },
                shape = CircleShape,
                containerColor = Green
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Transaction, User, or Period",
                    tint = Color.White
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        if (userTypeShowBottomSheet) {
            DialogUserType(
                modifier = Modifier,
                sheetState = userTypeSheetState,
                scope = userTypeScope,
                onBottomSheetChange = { userTypeShowBottomSheet = it },
                onChooseUserType = onUserTypeChange
            )
        }

        if (periodShowBottomSheet) {
            DialogPeriods(
                modifier = Modifier,
                sheetState = periodSheetState,
                scope = periodScope,
                periods = periods,
                onBottomSheetChange = { periodShowBottomSheet = it },
                onChoosePeriod = {
                    period = it
                    onPeriodChange(it)
                }
            )
        }

        LazyColumn(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileSection(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(top = paddingValues.calculateTopPadding()),
                    userType = userType,
                    role = role,
                    state = uiState.profile,
                    onListUserClick = onListUserClick,
                    onShowUserTypeSheet = { userTypeShowBottomSheet = true },
                    onRetry = onFetchProfile,
                    onLogout = { showLogoutDialog = true },
                    onClickUser = onUserCLick
                )
            }

            item {
                PeriodSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    period = period,
                    onClickAll = {
                        period = null
                    },
                    onShowPeriodSheet = { periodShowBottomSheet = true }
                )
            }

            item {
                TransactionSection(
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    state = uiState.transactions,
                    onAddTransaction = onAddTransaction,
                    onClickSeeMore = onSeeMoreTransaction
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewHomeContent() {
    HomeContent(
        periods = listOf(
            PeriodEntity(periodId = 0, "Bulan ke 1", "2025-08-06", "2025-09-05"),
        ),
        uiState = HomeUiState(
            profile = SectionState.Success(
                UserUIData(
                    id = 1,
                    username = "iqbalf",
                    fullname = "Iqbal Fauzi",
                    email = "",
                    phone = "",
                    domicile = "",
                    userType = "Admin",
                    imageProfileUrl = "",
                    isActive = true
                )
            ),
            periods = SectionState.Loading,
            transactions = SectionState.Success(
                data = listOf()
            )
        ),
        errorMessage = "",
        onListUserClick = {},
        onFetchProfile = {},
        onLogout = {},
        onPeriodChange = {},
        selectedPeriod = PeriodEntity(
            periodId = 0,
            "Bulan ke 1",
            "2025-08-06",
            "2025-09-05"
        ),
        userType = "Admin",
        onUserTypeChange = { },
        onSeeMoreTransaction = { },
        onAddTransaction = { },
        role = "",
        onUserCLick = {},
    )
}
