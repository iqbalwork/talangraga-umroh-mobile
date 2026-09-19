package com.talangraga.umrohmobile.presentation.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.ui.component.TransactionItem
import com.talangraga.umrohmobile.ui.utils.isWideScreen

@Composable
fun TransactionSection(
    modifier: Modifier = Modifier,
    showAllTransaction: Boolean = false,
    transactions: List<TransactionUiData>,
    onAddTransaction: () -> Unit,
    onClickSeeMore: () -> Unit,
    onTransactionClick: (TransactionUiData) -> Unit = {}
) {
    val isWide = isWideScreen()
    val displayTransactions = if (showAllTransaction) transactions else transactions.take(3)

    if (isWide && showAllTransaction) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 340.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayTransactions, key = { it.transactionId }) { transaction ->
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
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = if (isWide) 24.dp else 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(displayTransactions, key = { _, item -> item.transactionId }) { _, transaction ->
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

            if (!showAllTransaction) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClickSeeMore() }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Semua Tabungan",
                            style = TalangragaTypography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyTransaction(modifier: Modifier = Modifier, onAddTransaction: () -> Unit) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountBalanceWallet,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            modifier = Modifier.size(96.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Belum Ada Data Tabungan",
            style = TalangragaTypography.titleLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Semua riwayat transaksi dan tabungan akan muncul disini.",
            style = TalangragaTypography.bodyMedium.copy(
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onAddTransaction,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(text = "Tambah Tabungan")
            }
        }
    }
}
