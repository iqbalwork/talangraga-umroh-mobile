package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.talangraga.shared.Aqua
import com.talangraga.shared.Green
import com.talangraga.shared.MediumPurple
import com.talangraga.shared.RosePink
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.formatToIDR
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.ui.component.IconBlock
import com.talangraga.umrohmobile.ui.component.TransactionItem
import com.talangraga.umrohmobile.ui.section.CardInfoSection
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import com.talangraga.umrohmobile.ui.utils.isWideScreen

@Composable
fun HomeInfoTransactionSection(
    modifier: Modifier = Modifier,
    isHomeAdminDashboard: Boolean,
    state: SectionState<List<TransactionUiData>>,
    onAddTransaction: () -> Unit,
    onClickSeeMore: () -> Unit,
    onTransactionClick: (TransactionUiData) -> Unit = {}
) {
    val isWide = isWideScreen()

    when (state) {
        is SectionState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Gagal memuat data transaksi",
                    style = TalangragaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is SectionState.Loading -> {
            // Skeleton or loading placeholder handled by parent PullToRefresh
        }

        is SectionState.Success -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val transactions = state.data
                val transactionAvailable = transactions.isNotEmpty()
                val totalAmount = transactions.sumOf { it.amount }

                AnimatedVisibility(!transactionAvailable) {
                    EmptyTransactionSection(
                        modifier = Modifier.fillMaxWidth(),
                        onClickAddTabungan = onAddTransaction
                    )
                }

                if (transactionAvailable) {
                    val totalMember = transactions.distinctBy { it.reportedBy }.size
                    val average = transactions.map { it.amount }.average()

                    if (isWide) {
                        // Wide Screen: Responsive Row of Summary KPI Cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CardInfoSection(
                                modifier = Modifier.weight(1f),
                                title = "Total Tabungan Periode ini",
                                value = totalAmount.formatToIDR(),
                                icon = Icons.Default.Wallet,
                                illustrationIcon = Icons.Default.AttachMoney,
                                startIconColor = Aqua,
                                endIconColor = Green
                            )

                            if (isHomeAdminDashboard) {
                                CardInfoSection(
                                    modifier = Modifier.weight(1f),
                                    title = "Anggota Menabung",
                                    value = totalMember.toString(),
                                    notes = "Bulan ini",
                                    notesColor = MaterialTheme.colorScheme.primary,
                                    icon = Icons.Default.People,
                                    illustrationIcon = Icons.Default.AccountCircle,
                                    startIconColor = MediumPurple,
                                    endIconColor = MediumPurple
                                )
                            }

                            CardInfoSection(
                                modifier = Modifier.weight(1f),
                                title = "Rata-rata Tabungan",
                                value = average.toInt().formatToIDR(),
                                icon = Icons.Default.CreditCard,
                                illustrationIcon = Icons.Default.Calculate,
                                startIconColor = RosePink,
                                endIconColor = RosePink
                            )
                        }
                    } else {
                        // Phone Screen: Vertical stack of KPI cards
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CardInfoSection(
                                modifier = Modifier.fillMaxWidth(),
                                title = "Total Tabungan Periode ini",
                                value = totalAmount.formatToIDR(),
                                icon = Icons.Default.Wallet,
                                illustrationIcon = Icons.Default.AttachMoney,
                                startIconColor = Aqua,
                                endIconColor = Green
                            )

                            if (isHomeAdminDashboard) {
                                CardInfoSection(
                                    modifier = Modifier.fillMaxWidth(),
                                    title = "Anggota yang Menabung",
                                    value = totalMember.toString(),
                                    notes = "Bulan ini",
                                    notesColor = MaterialTheme.colorScheme.primary,
                                    icon = Icons.Default.People,
                                    illustrationIcon = Icons.Default.AccountCircle,
                                    startIconColor = MediumPurple,
                                    endIconColor = MediumPurple
                                )
                            }

                            CardInfoSection(
                                modifier = Modifier.fillMaxWidth(),
                                title = "Rata-rata Tabungan",
                                value = average.toInt().formatToIDR(),
                                icon = Icons.Default.CreditCard,
                                illustrationIcon = Icons.Default.Calculate,
                                startIconColor = RosePink,
                                endIconColor = RosePink
                            )
                        }
                    }

                    TransactionsList(
                        modifier = Modifier,
                        isHomeAdminDashboard = isHomeAdminDashboard,
                        transactions = transactions,
                        onClickSeeMore = onClickSeeMore,
                        onTransactionClick = onTransactionClick
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    transactions: List<TransactionUiData>,
    isHomeAdminDashboard: Boolean,
    onClickSeeMore: () -> Unit,
    onTransactionClick: (TransactionUiData) -> Unit = {}
) {
    val isWide = isWideScreen()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tabungan Terakhir",
                style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (!isHomeAdminDashboard && transactions.isNotEmpty()) {
                Text(
                    text = "Lihat Semua",
                    style = TalangragaTypography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClickSeeMore() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )

        AnimatedVisibility(
            visible = transactions.isEmpty(),
        ) {
            Text(
                text = "Tidak ada tabungan terkini.",
                style = TalangragaTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
        }

        AnimatedVisibility(
            visible = transactions.isNotEmpty(),
        ) {
            val displayList = transactions.take(if (isHomeAdminDashboard) transactions.size else 4)

            if (isWide && displayList.size > 1) {
                // 2-column adaptive layout on tablet
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    displayList.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { transaction ->
                                TransactionItem(
                                    modifier = Modifier.weight(1f),
                                    username = transaction.userName,
                                    paymentName = transaction.paymentName,
                                    paymentMethod = transaction.paymentType,
                                    date = transaction.transactionDate,
                                    amount = transaction.amount,
                                    status = transaction.statusTransaksi,
                                    onClick = { onTransactionClick(transaction) }
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    displayList.forEach { transaction ->
                        TransactionItem(
                            modifier = Modifier.fillMaxWidth(),
                            username = transaction.userName,
                            paymentName = transaction.paymentName,
                            paymentMethod = transaction.paymentType,
                            date = transaction.transactionDate,
                            amount = transaction.amount,
                            status = transaction.statusTransaksi,
                            onClick = { onTransactionClick(transaction) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyTransactionSection(modifier: Modifier = Modifier, onClickAddTabungan: () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconBlock(
                    icon = Icons.Default.SupervisorAccount,
                    startColor = MediumPurple,
                    endColor = MediumPurple,
                )
                IconBlock(
                    icon = Icons.Default.MoneyOff,
                    startColor = Aqua,
                    endColor = Green,
                )
                IconBlock(
                    icon = Icons.Default.Calculate,
                    startColor = RosePink,
                    endColor = RosePink,
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Belum ada data tabungan",
                style = TalangragaTypography.titleLarge.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Data tabungan dan anggota akan muncul disini setelah kamu mulai menambahkan tabungan.",
                style = TalangragaTypography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onClickAddTabungan,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Tambah Tabungan")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewEmptyTransactionSection() {
    TalangragaTheme {
        EmptyTransactionSection { }
    }
}
