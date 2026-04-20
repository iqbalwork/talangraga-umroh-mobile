package com.talangraga.umrohmobile.presentation.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.shared.Background
import com.talangraga.shared.BorderColor
import com.talangraga.shared.INDONESIA_TRIMMED
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.TextSecondaryDark
import com.talangraga.shared.formatDateRange
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.TextButton
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import com.talangraga.umrohmobile.ui.section.ListUserSheet
import com.talangraga.umrohmobile.ui.section.PeriodsSheet
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionScreen(
    rootNavController: NavHostController,
    navHostController: NavHostController,
    viewModel: TransactionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val transactionsList = (uiState.transactions as? SectionState.Success)?.data ?: emptyList()
    val periodsList = (uiState.periods as? SectionState.Success)?.data ?: emptyList()

    TransactionContent(
        isLoading = uiState.isLoading,
        onRefresh = {
            if (!uiState.isMember) {
                viewModel.onEvent(TransactionEvent.GetPeriods)
                viewModel.onEvent(TransactionEvent.GetUsers)
                viewModel.onEvent(TransactionEvent.GetTransactions(null, uiState.selectedUser?.id))
            } else {
                viewModel.onEvent(TransactionEvent.GetTransactions(null, uiState.selectedUser?.id))
            }
        },
        selectedPeriod = uiState.selectedPeriod,
        onPeriodChange = { viewModel.onEvent(TransactionEvent.SelectPeriod(it)) },
        periods = periodsList,
        transactions = transactionsList,
        onFetchAllTransaction = { viewModel.onEvent(TransactionEvent.GetTransactions()) },
        isMember = uiState.isMember,
        selectedUser = uiState.selectedUser,
        users = uiState.users,
        onSelectUser = { viewModel.onEvent(TransactionEvent.SelectUser(it)) },
        onTransactionClick = { transaction ->
            val transactionJson = Json.encodeToString(transaction)
            rootNavController.navigate(Screen.TransactionDetailRoute(transactionJson))
        },
        onAddTransaction = {
            navHostController.navigate(Screen.AddTransactionRoute(isCollective = false))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionContent(
    isLoading: Boolean = false,
    onRefresh: () -> Unit = {},
    isMember: Boolean = false,
    selectedUser: UserUIData?,
    users: List<UserUIData>,
    onSelectUser: (UserUIData?) -> Unit,
    selectedPeriod: PeriodEntity?,
    onPeriodChange: (PeriodEntity?) -> Unit,
    periods: List<PeriodEntity>,
    transactions: List<TransactionUiData>,
    onFetchAllTransaction: () -> Unit,
    onAddTransaction: () -> Unit,
    onTransactionClick: (TransactionUiData) -> Unit = {}
) {

    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var showPeriodBottom by remember { mutableStateOf(false) }

    val userSheetState = rememberModalBottomSheetState()
    val userScope = rememberCoroutineScope()
    var showUserSheet by remember { mutableStateOf(false) }

    val refreshState = rememberPullToRefreshState()

    if (showPeriodBottom) {
        PeriodsSheet(
            modifier = Modifier,
            sheetState = periodSheetState,
            scope = periodScope,
            periods = periods,
            onBottomSheetChange = { showPeriodBottom = it },
            onChoosePeriod = {
                onPeriodChange(it)
            }
        )
    }

    if (showUserSheet) {
        ListUserSheet(
            modifier = Modifier,
            sheetState = userSheetState,
            scope = userScope,
            data = users,
            onBottomSheetChange = { showUserSheet = it },
            onSelectUser = {
                onSelectUser(it)
            }
        )
    }

    TalangragaScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Riwayat Transaksi", style = TalangragaTypography.titleLarge)
                },
                modifier = Modifier,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = onRefresh,
                state = refreshState,
                modifier = Modifier.padding(paddingValues).fillMaxSize()
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(16.dp).fillMaxSize()
                ) {
                    val (filterRef, chooseUserRef, listTransactionRef, emptyRef) = createRefs()

                    if (!isMember) {
                        Row(
                            modifier = Modifier.constrainAs(filterRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                text = "Semua",
                                isSelected = selectedPeriod == null,
                                modifier = Modifier
                            ) {
                                onPeriodChange(null)
                            }
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
                                modifier = Modifier.weight(1f),
                            ) {
                                showPeriodBottom = true
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = null,
                                tint = TextSecondaryDark,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(1.dp, BorderColor, CircleShape)
                                    .clickable {
                                        showUserSheet = true
                                    }
                                    .background(color = Background)
                                    .padding(8.dp)
                            )
                        }

                        TextButtonOption(
                            text = selectedUser?.fullname ?: "Semua Pengguna",
                            placeholder = "Pilih Pengguna",
                            modifier = Modifier.constrainAs(chooseUserRef) {
                                top.linkTo(filterRef.bottom, 8.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            },
                        ) {
                            showUserSheet = true
                        }
                    }

                    AnimatedVisibility(
                        visible = transactions.isEmpty(),
                        modifier = Modifier.constrainAs(emptyRef) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        EmptyTransaction(modifier = Modifier, onAddTransaction = onAddTransaction)
                    }

                    AnimatedVisibility(
                        visible = transactions.isNotEmpty(),
                        modifier = Modifier.constrainAs(listTransactionRef) {
                            if (isMember) {
                                top.linkTo(parent.top)
                            } else {
                                top.linkTo(chooseUserRef.bottom)
                            }
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                        }
                    ) {
                        TransactionSection(
                            modifier = Modifier,
                            showAllTransaction = true,
                            transactions = transactions,
                            onAddTransaction = onAddTransaction,
                            onClickSeeMore = {

                            },
                            onTransactionClick = onTransactionClick
                        )
                    }
                }
            }

            if (transactions.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddTransaction,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Transaction",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TransactionContentPreview() {
    TalangragaTheme(useDynamicColor = false) {
        TransactionContent(
            transactions = emptyList(),
            onFetchAllTransaction = { },
            selectedPeriod = null,
            onPeriodChange = { },
            periods = emptyList(),
            selectedUser = null,
            users = emptyList(),
            onSelectUser = { },
            onAddTransaction = { },
            onTransactionClick = { }
        )
    }
}
