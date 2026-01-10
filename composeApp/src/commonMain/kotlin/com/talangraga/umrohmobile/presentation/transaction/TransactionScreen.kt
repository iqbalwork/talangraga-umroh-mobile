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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.talangraga.umrohmobile.presentation.home.HomeViewModel
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.TextButton
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionScreen(
    rootNavController: NavHostController,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = koinViewModel(),
    viewModel: TransactionViewModel = koinViewModel()
) {

    val transactions by homeViewModel.transactions.collectAsStateWithLifecycle()

    TransactionContent(
        period = homeViewModel.selectedPeriod.value,
        onPeriodChange = homeViewModel::setSelectedPeriod,
        transactions = transactions,
        onClickAll = {},
        onShowPeriodSheet = {},
        onFetchAllTransaction = { }
    ) {
        navHostController.navigate(Screen.AddTransactionRoute(isCollective = false))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionContent(
    period: PeriodEntity?,
    onPeriodChange: (PeriodEntity?) -> Unit,
    transactions: List<TransactionUiData>,
    onFetchAllTransaction: () -> Unit,
    onClickAll: () -> Unit,
    onShowPeriodSheet: () -> Unit,
    onAddTransaction: () -> Unit,
) {

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
            ConstraintLayout(
                modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()
            ) {
                val (filterRef, chooseUserRef, listTransactionRef, emptyRef) = createRefs()

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
                        isSelected = period == null,
                        modifier = Modifier
                    ) {
                        onClickAll()
                    }
                    val bulan = if (period != null) {
                        formatDateRange(
                            startDateString = period.startDate.orEmpty(),
                            endDateString = period.endDate.orEmpty(),
                            monthFormat = INDONESIA_TRIMMED
                        )
                    } else ""
                    TextButtonOption(
                        text = bulan,
                        placeholder = "Pilih Bulan",
                        trailingIcon = Icons.Default.ArrowDropDown,
                        modifier = Modifier.weight(1f),
                    ) { onShowPeriodSheet() }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        tint = TextSecondaryDark,
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, BorderColor, CircleShape)
                            .clickable {

                            }
                            .background(color = Background)
                            .padding(8.dp)
                    )
                }

                TextButtonOption(
                    text = "",
                    placeholder = "Pilih Pengguna",
                    modifier = Modifier.constrainAs(chooseUserRef) {
                        top.linkTo(filterRef.bottom, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                ) { }

                AnimatedVisibility(
                    visible = transactions.isEmpty(),
                    modifier = Modifier.constrainAs(createRef()) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ) {
                    EmptyTransaction(modifier = Modifier) {
                        onAddTransaction()
                    }
                }

                AnimatedVisibility(
                    visible = transactions.isNotEmpty(),
                    modifier = Modifier.constrainAs(listTransactionRef) {
                        top.linkTo(chooseUserRef.bottom)
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
                        onAddTransaction = {

                        },
                        onClickSeeMore = {

                        }
                    )
                }
            }

            if (transactions.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { onAddTransaction() },
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
            onClickAll = {}, onShowPeriodSheet = {},
            onFetchAllTransaction = { },
            period = null,
            onPeriodChange = { }
        ) {

        }
    }
}
