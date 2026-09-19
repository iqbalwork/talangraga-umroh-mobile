package com.talangraga.umrohmobile.presentation.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.shared.INDONESIA_TRIMMED
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.cleanPeriodName
import com.talangraga.shared.formatDateRange
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.rememberFileExporter
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.TextButton
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.section.ListUserSheet
import com.talangraga.umrohmobile.ui.section.PeriodsSheet
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import com.talangraga.umrohmobile.ui.utils.isWideScreen
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material3.TextButton as M3TextButton

@Composable
fun TransactionScreen(
    rootNavController: NavHostController,
    navHostController: NavHostController,
    viewModel: TransactionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val transactionsList = (uiState.transactions as? SectionState.Success)?.data ?: emptyList()
    val periodsList = (uiState.periods as? SectionState.Success)?.data ?: emptyList()

    val fileExporter = rememberFileExporter()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TransactionEffect.ShowError -> {
                    ToastManager.show(message = effect.message, type = ToastType.Error)
                }
                is TransactionEffect.ShowMessage -> {
                    ToastManager.show(message = effect.message, type = ToastType.Success)
                }
                is TransactionEffect.ExportSuccess -> {
                    val success = fileExporter.exportAndShare(effect.bytes, effect.fileName)
                    if (success) {
                        ToastManager.show(
                            message = "File ${effect.fileName} berhasil diekspor & dibagikan",
                            type = ToastType.Success
                        )
                    } else {
                        ToastManager.show(
                            message = "File ${effect.fileName} berhasil diekspor (${effect.bytes.size / 1024} KB)",
                            type = ToastType.Success
                        )
                    }
                }
            }
        }
    }

    TransactionContent(
        isLoading = uiState.isLoading,
        isExporting = uiState.isExporting,
        isImporting = uiState.isImporting,
        importResult = uiState.importResult,
        onRefresh = {
            if (!uiState.isMember) {
                viewModel.onEvent(TransactionEvent.GetPeriods)
                viewModel.onEvent(TransactionEvent.GetUsers)
            }
            viewModel.onEvent(
                TransactionEvent.GetTransactions(
                    uiState.selectedPeriod?.periodId,
                    uiState.selectedUser?.id
                )
            )
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
        onExport = { format -> viewModel.onEvent(TransactionEvent.ExportTransactions(format)) },
        onImport = { bytes, fileName -> viewModel.onEvent(TransactionEvent.ImportTransactions(bytes, fileName)) },
        onDismissImportResult = { viewModel.onEvent(TransactionEvent.DismissImportResult) },
        onTransactionClick = { transaction ->
            val transactionJson = Json.encodeToString(transaction)
            navHostController.navigate(Screen.TransactionDetailRoute(transactionJson))
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
    isExporting: Boolean = false,
    isImporting: Boolean = false,
    importResult: ImportResultUiData? = null,
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
    onExport: (format: String) -> Unit = {},
    onImport: (fileBytes: ByteArray, fileName: String) -> Unit = { _, _ -> },
    onDismissImportResult: () -> Unit = {},
    onTransactionClick: (TransactionUiData) -> Unit = {}
) {
    val isWide = isWideScreen()
    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var showPeriodBottom by remember { mutableStateOf(false) }

    val userSheetState = rememberModalBottomSheetState()
    val userScope = rememberCoroutineScope()
    var showUserSheet by remember { mutableStateOf(false) }

    var showExportSheet by remember { mutableStateOf(false) }
    val exportSheetState = rememberModalBottomSheetState()

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

    // Export Options BottomSheet
    if (showExportSheet) {
        ModalBottomSheet(
            onDismissRequest = { showExportSheet = false },
            sheetState = exportSheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Export Data Tabungan",
                    style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Pilih format dokumen laporan tabungan yang ingin diunduh:",
                    style = TalangragaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Option 1: Excel
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .clickable {
                            showExportSheet = false
                            onExport("excel")
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "Export ke Excel (.xlsx)",
                            style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tabel rekapitulasi data dan formula total saldo",
                            style = TalangragaTypography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Option 2: PDF
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .clickable {
                            showExportSheet = false
                            onExport("pdf")
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Column {
                        Text(
                            text = "Export ke PDF (.pdf)",
                            style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Dokumen resmi siap cetak dan dibagikan",
                            style = TalangragaTypography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(Modifier.height(28.dp))
            }
        }
    }

    // Import Result Summary Dialog
    if (importResult != null) {
        AlertDialog(
            onDismissRequest = onDismissImportResult,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (importResult.failedCount == 0) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (importResult.failedCount == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Hasil Import Tabungan",
                        style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Total Data Diproses: ${importResult.totalRows}",
                        style = TalangragaTypography.bodyMedium
                    )
                    Text(
                        text = "✅ Berhasil Diimpor: ${importResult.successCount}",
                        style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (importResult.failedCount > 0) {
                        Text(
                            text = "❌ Gagal / Tidak Valid: ${importResult.failedCount}",
                            style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Rincian Error:",
                            style = TalangragaTypography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        importResult.errors.take(5).forEach { err ->
                            Text(
                                text = "• Baris ${err.row}: ${err.error}",
                                style = TalangragaTypography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            },
            confirmButton = {
                M3TextButton(onClick = onDismissImportResult) {
                    Text("Tutup", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    TalangragaScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Riwayat Tabungan",
                        style = TalangragaTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    if (isExporting || isImporting) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 12.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        // Export Action Icon
                        IconButton(onClick = { showExportSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Export Tabungan",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            if (transactions.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddTransaction,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = if (isWide) 16.dp else 100.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Transaction"
                    )
                }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (!isMember) {
                    // Filter Row for Periods & Members
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                            text = if (selectedPeriod != null) "${selectedPeriod.periodeName.cleanPeriodName()}: $bulan" else "Pilih Bulan",
                            placeholder = "Pilih Bulan",
                            trailingIcon = Icons.Default.ArrowDropDown,
                            modifier = Modifier.weight(1f),
                        ) {
                            showPeriodBottom = true
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButtonOption(
                            text = selectedUser?.fullname ?: "Semua Pengguna",
                            placeholder = "Pilih Pengguna",
                            modifier = Modifier.weight(1f),
                        ) {
                            showUserSheet = true
                        }

                        if (selectedUser != null) {
                            IconButton(
                                onClick = { onSelectUser(null) },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear User",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }

                if (transactions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyTransaction(onAddTransaction = onAddTransaction)
                    }
                } else {
                    TransactionSection(
                        modifier = Modifier.fillMaxSize(),
                        showAllTransaction = true,
                        transactions = transactions,
                        onAddTransaction = onAddTransaction,
                        onClickSeeMore = { },
                        onTransactionClick = onTransactionClick
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
