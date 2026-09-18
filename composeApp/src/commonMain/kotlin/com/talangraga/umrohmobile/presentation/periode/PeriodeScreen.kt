package com.talangraga.umrohmobile.presentation.periode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.section.AddPeriodeSheet
import com.talangraga.umrohmobile.ui.section.PeriodItem
import com.talangraga.umrohmobile.ui.utils.isWideScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PeriodeScreen(
    navHostController: NavHostController,
    viewModel: PeriodeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showAddPeriodeSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PeriodeEffect.ShowToastError -> {
                    ToastManager.show(message = effect.message, type = ToastType.Error)
                    viewModel.onEvent(PeriodeEvent.ClearError)
                }

                is PeriodeEffect.ShowToastSuccess -> {
                    ToastManager.show(message = effect.message, type = ToastType.Success)
                    showAddPeriodeSheet = false
                }
            }
        }
    }

    PeriodeContent(
        isLoading = uiState.isLoading,
        periods = uiState.periods,
        onRefresh = { viewModel.onEvent(PeriodeEvent.GetPeriods) },
        onAddClick = { showAddPeriodeSheet = true }
    )

    if (showAddPeriodeSheet) {
        AddPeriodeSheet(
            isLoading = uiState.isLoading,
            onDismissRequest = { showAddPeriodeSheet = false },
            onSubmit = { name, start, end ->
                viewModel.onEvent(PeriodeEvent.AddPeriode(name, start, end))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodeContent(
    isLoading: Boolean = false,
    periods: List<PeriodUiModel> = emptyList(),
    onRefresh: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val isWide = isWideScreen()
    val refreshState = rememberPullToRefreshState()

    TalangragaScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Periode Tabungan",
                        style = TalangragaTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = if (isWide) 16.dp else 100.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Periode")
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = onRefresh,
            state = refreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            if (isWide) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 320.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        items = periods,
                        key = { _, item -> item.period.periodId }
                    ) { index, item ->
                        val number = Regex("\\d+").find(item.period.periodeName)?.value?.toIntOrNull()
                        PeriodItem(
                            isCurrent = item.isActive,
                            periodNumber = number ?: (index + 1),
                            period = item.period,
                            totalAmount = item.totalAmount,
                            transactionCount = item.transactionCount,
                            onPeriodClick = { }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 120.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        items = periods,
                        key = { _, item -> item.period.periodId }
                    ) { index, item ->
                        val number = Regex("\\d+").find(item.period.periodeName)?.value?.toIntOrNull()
                        PeriodItem(
                            isCurrent = item.isActive,
                            periodNumber = number ?: (index + 1),
                            period = item.period,
                            totalAmount = item.totalAmount,
                            transactionCount = item.transactionCount,
                            onPeriodClick = { }
                        )
                    }
                }
            }
        }
    }
}
