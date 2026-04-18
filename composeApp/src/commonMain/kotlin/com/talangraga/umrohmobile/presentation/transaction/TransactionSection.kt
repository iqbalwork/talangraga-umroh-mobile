package com.talangraga.umrohmobile.presentation.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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

@Composable
fun TransactionSection(
    modifier: Modifier = Modifier,
    showAllTransaction: Boolean = false,
    transactions: List<TransactionUiData>,
    onAddTransaction: () -> Unit,
    onClickSeeMore: () -> Unit,
    onTransactionClick: (TransactionUiData) -> Unit = {}
) {

    val displayTransactions = if (showAllTransaction) transactions else transactions.take(3)

    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(displayTransactions) { transaction ->
            TransactionItem(
                modifier = Modifier.fillMaxWidth(),
                username = transaction.userName,
                paymentName = transaction.paymentName,
                paymentMethod = transaction.paymentType,
                date = transaction.transactionDate,
                amount = transaction.amount,
                onClick = { onTransactionClick(transaction) }
            )
        }
        if (!showAllTransaction) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClickSeeMore()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Semua transaksi", style = TalangragaTypography.bodySmall)
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun EmptyTransaction(modifier: Modifier = Modifier, onAddTransaction: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Folder,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = "Belum ada data tabungan",
            style = TalangragaTypography.titleLarge.copy(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Semua tabungan akan muncul disini.",
            style = TalangragaTypography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onAddTransaction,
            modifier = Modifier
        ) {
            Text(
//                        text = stringResource(Res.string.login),
                text = "Tambah Tabungan",
            )
        }
    }
}
