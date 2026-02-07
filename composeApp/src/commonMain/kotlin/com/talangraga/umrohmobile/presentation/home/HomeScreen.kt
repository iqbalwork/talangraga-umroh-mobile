package com.talangraga.umrohmobile.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.presentation.home.section.HomeInfoTransactionSection
import com.talangraga.umrohmobile.presentation.home.section.PeriodSection
import com.talangraga.umrohmobile.presentation.home.section.ProfileSection
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import com.talangraga.umrohmobile.ui.component.ImageViewerManager
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.section.DialogPeriods
import com.talangraga.umrohmobile.ui.section.DialogUserType
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    rootNavHostController: NavHostController,
    onNavigateToTransaction: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {

    val periods by viewModel.periods.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userProfile by viewModel.session.userProfile.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    HomeContent(
        user = userProfile?.toUiData(),
        userType = userProfile?.userType.orEmpty(),
        onUserTypeChange = viewModel::setUserType,
        periods = periods,
        selectedPeriod = viewModel.selectedPeriod.value,
        uiState = uiState,
        errorMessage = errorMessage.orEmpty(),
        onPeriodChange = {
            viewModel.setSelectedPeriod(it)
            viewModel.getTransactions(it?.periodId)
        },
        onFetchProfile = viewModel::getProfile,
        onSeeMoreTransaction = onNavigateToTransaction,
        onAddTransaction = {
            rootNavHostController.navigate(Screen.AddTransactionRoute(false))
        },
        onFetchAllTransaction = {
            viewModel.getTransactions()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    user: UserUIData?,
    userType: String,
    onUserTypeChange: (String) -> Unit,
    periods: List<PeriodEntity>,
    selectedPeriod: PeriodEntity?,
    uiState: HomeUiState,
    errorMessage: String,
    onPeriodChange: (PeriodEntity?) -> Unit,
    onSeeMoreTransaction: () -> Unit,
    onAddTransaction: () -> Unit,
    onFetchProfile: () -> Unit,
    onFetchAllTransaction: () -> Unit,
) {

    val refreshState = rememberPullToRefreshState()

    val userTypeSheetState = rememberModalBottomSheetState()
    val userTypeScope = rememberCoroutineScope()
    var userTypeShowBottomSheet by remember { mutableStateOf(false) }
    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var showPeriodeBottom by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        scope.launch {
            if (errorMessage.isNotEmpty()) snackbarHostState.showSnackbar(errorMessage)
        }
    }

    TalangragaScaffold(
        contentWindowInsets = WindowInsets.statusBars,
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

        if (showPeriodeBottom) {
            DialogPeriods(
                modifier = Modifier,
                sheetState = periodSheetState,
                scope = periodScope,
                periods = periods,
                onBottomSheetChange = { showPeriodeBottom = it },
                onChoosePeriod = {
                    onPeriodChange(it)
                }
            )
        }

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = {
                onFetchProfile()
                if (selectedPeriod != null) {
                    onPeriodChange(selectedPeriod)
                } else {
                    onFetchAllTransaction()
                }
            },
            state = refreshState,
            modifier = Modifier
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ProfileSection(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(top = paddingValues.calculateTopPadding()),
                        userType = userType,
                        user = user,
//                        state = uiState.profile,
                        onRetry = onFetchProfile,
                        onClickImage = {
                            ImageViewerManager.show(it)
                        },
                    )
                }

                item {
                    PeriodSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        period = selectedPeriod,
                        onClickAll = {
                            onPeriodChange(null)
                            onFetchAllTransaction()
                        },
                        onShowPeriodSheet = { showPeriodeBottom = true }
                    )
                }

                item {
                    HomeInfoTransactionSection(
                        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                        isHomeAdminDashboard = true,
                        state = uiState.transactions,
                        onAddTransaction = onAddTransaction,
                        onClickSeeMore = onSeeMoreTransaction
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewHomeContent() {
    TalangragaTheme {
        HomeContent(
            periods = listOf(
                PeriodEntity(periodId = 0, "Bulan ke 1", "2025-08-06", "2025-09-05"),
            ),
            user = UserUIData(
                id = 1,
                username = "iqbalf",
                fullname = "Iqbal Fauzi",
                email = "",
                phone = "",
                domicile = "",
                userType = "Admin",
                imageProfileUrl = "",
                isActive = true
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
            onFetchProfile = {},
            onPeriodChange = {},
            userType = "Admin",
            onUserTypeChange = { },
            onSeeMoreTransaction = { },
            onAddTransaction = { },
            onFetchAllTransaction = {},
            selectedPeriod = null,
        )
    }
}
