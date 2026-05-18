package com.talangraga.umrohmobile.presentation.periode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    periods: List<PeriodEntity> = emptyList(),
    onRefresh: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val refreshState = rememberPullToRefreshState()

    TalangragaScaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Periode Tabungan", style = TalangragaTypography.titleLarge)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = periods,
                    key = { _, item -> item.periodId }
                ) { index, item ->
                    val number = Regex("\\d+").find(item.periodeName)?.value?.toIntOrNull()
                    PeriodItem(
                        periodNumber = number ?: (index + 1),
                        period = item,
                        onPeriodClick = {
                            // Handle period click if needed
                        }
                    )
                }
            }
        }
    }
}
