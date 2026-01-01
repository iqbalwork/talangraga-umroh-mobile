package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.shared.Aqua
import com.talangraga.shared.Green
import com.talangraga.shared.MediumPurple
import com.talangraga.shared.PorcelainDark
import com.talangraga.shared.RosePink
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.formatToIDR
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.component.IconBlock
import com.talangraga.umrohmobile.ui.component.TransactionItem
import com.talangraga.umrohmobile.ui.section.CardInfoSection
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeInfoTransactionSection(
    modifier: Modifier = Modifier,
    state: SectionState<List<TransactionUiData>>,
    onAddTransaction: () -> Unit,
    onClickSeeMore: () -> Unit
) {
    when (state) {
        is SectionState.Error -> {

        }

        is SectionState.Loading -> {

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
                    CardInfoSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Total Tabungan Periode ini",
                        value = if (transactionAvailable) totalAmount.formatToIDR() else "Belum Ada Transaksi",
                        icon = Icons.Default.Wallet,
                        illustrationIcon = Icons.Default.AttachMoney,
                        startIconColor = Aqua,
                        endIconColor = Green
                    )
                    val totalMember = transactions.distinctBy { it.reportedBy }.size
                    val average = transactions.map { it.amount }.average()
                    CardInfoSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Anggota yang Menabung",
                        value = if (transactionAvailable) totalMember.toString() else "Belum ada yang menabung",
                        notes = if (transactionAvailable) "Bulan ini" else "",
                        notesColor = PorcelainDark,
                        icon = Icons.Default.People,
                        illustrationIcon = Icons.Default.AccountCircle,
                        startIconColor = MediumPurple,
                        endIconColor = MediumPurple
                    )
                    CardInfoSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Rata-rata Tabungan",
                        value = average.toInt().formatToIDR(),
                        icon = Icons.Default.CreditCard,
                        illustrationIcon = Icons.Default.Calculate,
                        startIconColor = RosePink,
                        endIconColor = RosePink
                    )

                    HomeInfoTransactionSection(
                        modifier = Modifier,
                        transactions = transactions,
                        onClickSeeMore = onClickSeeMore
                    )
                }
            }
        }
    }
}

@Composable
fun HomeInfoTransactionSection(
    modifier: Modifier = Modifier,
    transactions: List<TransactionUiData>,
    onClickSeeMore: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Spacing between items
    ) {

        Text(
            text = "Transaksi Terakhir",
            style = TalangragaTypography.titleLarge,
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            thickness = 1.dp
        )

        AnimatedVisibility(
            visible = transactions.isEmpty(),
        ) {
            Text(
                text = "Tidak ada transaksi terkini.",
                style = TalangragaTypography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
        }

        AnimatedVisibility(
            transactions.isNotEmpty(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                transactions.take(3).forEach { transaction ->
                    TransactionItem(
                        modifier = Modifier.fillMaxWidth(),
                        username = transaction.reportedBy,
                        paymentName = transaction.paymentName,
                        paymentMethod = transaction.paymentType,
                        date = transaction.transactionDate,
                        amount = transaction.amount
                    )
                }
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
fun EmptyTransactionSection(modifier: Modifier = Modifier, onClickAddTabungan: () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceBright)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val (iconRef, titleRef, descRef) = createRefs()
            Row(
                modifier = Modifier.constrainAs(iconRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
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
                    modifier = Modifier
                )
                IconBlock(
                    icon = Icons.Default.Calculate,
                    startColor = RosePink,
                    endColor = RosePink,
                )
            }
            Text(
                text = "Belum ada data tabungan",
                style = TalangragaTypography.titleLarge.copy(
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(titleRef) {
                        top.linkTo(iconRef.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                text = "Data tabungan dan anggota akan muncul disini setelah kamu mulai menambahkan tabungan.",
                style = TalangragaTypography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(descRef) {
                        top.linkTo(titleRef.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Button(
                onClick = onClickAddTabungan,
                modifier = Modifier.constrainAs(createRef()) {
                    top.linkTo(descRef.bottom, 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
//                        text = stringResource(Res.string.login),
                        text = "Tambah Tabungan",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewEmptyTransactionSection() {
    EmptyTransactionSection { }
}

@Preview
@Composable
fun PreviewTransactionSection() {
    val dummyTransactions = listOf(
        TransactionUiData(
            transactionId = 1,
            amount = 500000,
            transactionDate = "2025-08-29T22:15:00.000Z",
            statusTransaksi = "",
            reportedDate = "2025-08-29T22:15:00.000Z",
            buktiTransferUrl = "",
            paymentType = "Bank Transfer",
            paymentName = "BCA",
            reportedBy = "Iqbal Fauzi",
            confirmedBy = ""
        ),
        TransactionUiData(
            transactionId = 2,
            amount = 250000,
            transactionDate = "2025-08-29T22:15:00.000Z",
            statusTransaksi = "",
            reportedDate = "2025-08-29T22:15:00.000Z",
            buktiTransferUrl = "",
            paymentType = "Bank Transfer",
            paymentName = "BCA",
            reportedBy = "Iqbal Fauzi",
            confirmedBy = ""
        ),
        TransactionUiData(
            transactionId = 3,
            amount = 200000,
            transactionDate = "2025-08-29T22:15:00.000Z",
            statusTransaksi = "",
            reportedDate = "2025-08-29T22:15:00.000Z",
            buktiTransferUrl = "",
            paymentType = "Bank Transfer",
            paymentName = "BCA",
            reportedBy = "Iqbal Fauzi",
            confirmedBy = ""
        )
    )
    TalangragaTheme(useDynamicColor = false) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HomeInfoTransactionSection(transactions = dummyTransactions) {}
            HomeInfoTransactionSection(transactions = emptyList()) {} // Preview for empty state
        }
    }
}
