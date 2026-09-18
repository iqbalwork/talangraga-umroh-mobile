package com.talangraga.umrohmobile.presentation.transaction.detailtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.talangraga.shared.Green
import com.talangraga.shared.Orange
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.cleanPeriodName
import com.talangraga.shared.formatDateRange
import com.talangraga.shared.formatIsoTimestampToCustom
import com.talangraga.shared.formatToIDR
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.ImageViewerManager
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TransactionDetailScreen(
    transaction: TransactionUiData,
    onBackClick: () -> Unit,
    viewModel: TransactionDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(enabled = !ImageViewerManager.isVisible) {
        onBackClick()
    }

    LaunchedEffect(transaction) {
        viewModel.onEvent(TransactionDetailEvent.SetInitialData(transaction))
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is TransactionDetailEffect.ShowToastSuccess -> {
                    ToastManager.show(
                        message = effect.message,
                        type = ToastType.Success
                    )
                }
                is TransactionDetailEffect.ShowToastError -> {
                    ToastManager.show(
                        message = effect.message,
                        type = ToastType.Error
                    )
                }
                TransactionDetailEffect.NavigateBack -> {
                    onBackClick()
                }
            }
        }
    }

    val currentTransaction = uiState.transaction ?: transaction
    val isActionAvailable = uiState.isAdmin && currentTransaction.statusTransaksi.lowercase() != "completed"

    val (statusBgColor, statusBorderColor, statusTextColor, statusText) = when (currentTransaction.statusTransaksi.lowercase()) {
        "completed" -> StatusBadgeColors(
            bgColor = Green.copy(alpha = 0.15f),
            borderColor = Green.copy(alpha = 0.5f),
            textColor = Green,
            label = "Disetujui"
        )
        "on_process" -> StatusBadgeColors(
            bgColor = Orange.copy(alpha = 0.15f),
            borderColor = Orange.copy(alpha = 0.5f),
            textColor = Orange,
            label = "Sedang Diproses"
        )
        else -> StatusBadgeColors(
            bgColor = Sage.copy(alpha = 0.15f),
            borderColor = Sage.copy(alpha = 0.5f),
            textColor = Sage,
            label = "Menunggu Verifikasi"
        )
    }

    TalangragaScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detail Tabungan",
                        style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            if (isActionAvailable) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        if (currentTransaction.statusTransaksi.lowercase() == "sent") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Tandai Diproses Button
                                FilledTonalButton(
                                    onClick = {
                                        viewModel.onEvent(TransactionDetailEvent.UpdateStatus("on_process"))
                                    },
                                    enabled = !uiState.isUpdating,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = Orange.copy(alpha = 0.18f),
                                        contentColor = Orange
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                ) {
                                    if (uiState.isUpdating) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = Orange
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = Orange,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Diproses",
                                            style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Orange
                                        )
                                    }
                                }

                                // Setujui Setoran Button
                                Button(
                                    onClick = {
                                        viewModel.onEvent(TransactionDetailEvent.UpdateStatus("completed"))
                                    },
                                    enabled = !uiState.isUpdating,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Green,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                ) {
                                    if (uiState.isUpdating) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = Color.White
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Setujui",
                                            style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        } else {
                            // Single full-width Setujui Setoran Button (when on_process)
                            Button(
                                onClick = {
                                    viewModel.onEvent(TransactionDetailEvent.UpdateStatus("completed"))
                                },
                                enabled = !uiState.isUpdating,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Green,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                if (uiState.isUpdating) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = Color.White
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Setujui Setoran",
                                        style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Status Badge Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(statusBgColor)
                        .border(1.dp, statusBorderColor, RoundedCornerShape(50.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusTextColor)
                        )
                        Text(
                            text = statusText,
                            style = TalangragaTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = statusTextColor
                        )
                    }
                }

                // Highlight Nominal Banner Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "NOMINAL SETORAN",
                                    style = TalangragaTypography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentTransaction.amount.formatToIDR(),
                                    style = TalangragaTypography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = Sage
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "ID Setoran: #${currentTransaction.transactionId}",
                                    style = TalangragaTypography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(0.9f)
                            ) {
                                Text(
                                    text = "Tanggal Setor",
                                    style = TalangragaTypography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = currentTransaction.transactionDate.formatIsoTimestampToCustom(),
                                    style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.End
                                )
                                if (currentTransaction.reportedDate.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Dilaporkan: ${currentTransaction.reportedDate.formatIsoTimestampToCustom()}",
                                        style = TalangragaTypography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline,
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }

                // Data Anggota Card (Full Width Column)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Sage,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "DATA ANGGOTA",
                                style = TalangragaTypography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = currentTransaction.userName.ifBlank { "Anggota" },
                            style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (currentTransaction.reportedBy.isNotBlank() && currentTransaction.reportedBy != currentTransaction.userName) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Diinput oleh:",
                                    style = TalangragaTypography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = currentTransaction.reportedBy,
                                    style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Periode & Pembayaran Card (Full Width Column)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = null,
                                tint = Orange,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "PERIODE & PEMBAYARAN",
                                style = TalangragaTypography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Periode Tabungan",
                                style = TalangragaTypography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                            val cleanPeriod = currentTransaction.periodName.cleanPeriodName()
                            val periodRange = formatDateRange(currentTransaction.periodStartDate, currentTransaction.periodEndDate)
                            val periodDisplay = when {
                                cleanPeriod.isNotBlank() && periodRange.isNotBlank() -> "$cleanPeriod ($periodRange)"
                                cleanPeriod.isNotBlank() -> cleanPeriod
                                else -> "Semua Periode"
                            }
                            Text(
                                text = periodDisplay,
                                style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Metode Pembayaran",
                                style = TalangragaTypography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                            val paymentDisplayText = if (currentTransaction.paymentType.isNotBlank() && currentTransaction.paymentType != currentTransaction.paymentName) {
                                "${currentTransaction.paymentName} (${currentTransaction.paymentType})"
                            } else {
                                currentTransaction.paymentName.ifBlank { "Transfer Bank" }
                            }
                            Text(
                                text = paymentDisplayText,
                                style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Bukti Transfer Section
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "BUKTI TRANSFER",
                        style = TalangragaTypography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    if (currentTransaction.buktiTransferUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 10f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                    RoundedCornerShape(16.dp)
                                )
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicImage(
                                model = currentTransaction.buktiTransferUrl,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        ImageViewerManager.show(currentTransaction.buktiTransferUrl)
                                    }
                            )

                            // Preview Zoom Overlay Button
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Black.copy(alpha = 0.65f))
                                    .clickable {
                                        ImageViewerManager.show(currentTransaction.buktiTransferUrl)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ZoomIn,
                                        contentDescription = "Perbesar Gambar",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Perbesar Gambar",
                                        style = TalangragaTypography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Upload,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Tidak ada lampiran foto bukti transfer",
                                    style = TalangragaTypography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

private data class StatusBadgeColors(
    val bgColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val label: String
)

@Preview
@Composable
fun TransactionDetailScreenPreview() {
    TalangragaTheme {
        TransactionDetailScreen(
            transaction = TransactionUiData(
                transactionId = 4,
                amount = 2000000,
                transactionDate = "2026-02-20T18:50:00.000Z",
                statusTransaksi = "on_process",
                reportedDate = "2026-02-20T18:50:00.000Z",
                reportedBy = "Admin Talangraga",
                confirmedBy = "",
                buktiTransferUrl = "",
                paymentType = "Virtual Account",
                paymentName = "Mandiri Virtual Account",
                userName = "Siti Rahma",
                userId = "2",
                periodId = 2,
                periodName = "Periode 2",
                periodStartDate = "2026-02-06",
                periodEndDate = "2026-03-05"
            ),
            onBackClick = {}
        )
    }
}
