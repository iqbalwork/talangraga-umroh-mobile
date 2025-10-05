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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.ui.Aqua
import com.talangraga.umrohmobile.ui.Green
import com.talangraga.umrohmobile.ui.MediumPurple
import com.talangraga.umrohmobile.ui.RosePink
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.TalangragaTypography
import com.talangraga.umrohmobile.ui.component.IconBlock
import com.talangraga.umrohmobile.ui.section.CardInfoSection
import com.talangraga.umrohmobile.util.formatIsoTimestampToCustom
import com.talangraga.umrohmobile.util.formatToIDR
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TransactionSection(
    modifier: Modifier = Modifier,
    state: SectionState<List<TransactionEntity>>,
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
                        notes = if (transactionAvailable) "12% dari bulan lalu" else "",
                        notesColor = Green,
                        icon = Icons.Default.AttachMoney,
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
                        notesColor = MediumPurple,
                        icon = Icons.Default.AccountCircle,
                        startIconColor = MediumPurple,
                        endIconColor = MediumPurple
                    )
                    CardInfoSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Rata-rata Tabungan",
                        value = average.toInt().formatToIDR(),
                        notes = "Per Anggota/Bulan",
                        notesColor = RosePink,
                        icon = Icons.Default.Calculate,
                        startIconColor = RosePink,
                        endIconColor = RosePink
                    )

                    TransactionSection(
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
fun TransactionSection(
    modifier: Modifier = Modifier,
    transactions: List<TransactionEntity>,
    onClickSeeMore: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            ConstraintLayout(
                modifier = Modifier
                    .clickable(onClick = onClickSeeMore)
                    .fillMaxWidth()
            ) {
                val (titleRef, subtitleRef, seeAllRef) = createRefs()

                Text(
                    text = "Transaksi Terakhir",
                    style = TalangragaTypography.titleMedium,
                    modifier = Modifier
                        .constrainAs(titleRef) {
                            top.linkTo(parent.top, 16.dp)
                            start.linkTo(parent.start, 16.dp)
                        }
                )

                Text(
                    text = "Tekan disini untuk melihat semua transaksi",
                    style = TalangragaTypography.bodySmall.copy(
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                        .constrainAs(subtitleRef) {
                            top.linkTo(titleRef.bottom)
                            start.linkTo(titleRef.start)
                        }
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "Expand User Type",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(32.dp)
                        .constrainAs(seeAllRef) {
                            top.linkTo(titleRef.top)
                            end.linkTo(parent.end, 16.dp)
                            bottom.linkTo(subtitleRef.bottom)
                        }
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ), // Padding around the list of items
                verticalArrangement = Arrangement.spacedBy(12.dp) // Spacing between items
            ) {
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
                    transactions.forEach { transaction ->
                        TransactionItem(
                            modifier = Modifier.fillMaxWidth(),
                            username = transaction.reportedBy, // Assuming reportedBy is the username
                            amount = transaction.amount,
                            date = transaction.transactionDate
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(modifier: Modifier = Modifier, username: String, amount: Int, date: String) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(0xFFF5F5F5)) // Light gray background for the item
            .padding(12.dp) // Internal padding for the item
            .fillMaxWidth()
    ) {
        val (usernameRef, amountRef, dateRef) = createRefs()

        Text(
            text = username,
            style = TalangragaTypography.titleMedium,
            modifier = Modifier.constrainAs(usernameRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )

        Text(
            text = date.formatIsoTimestampToCustom(),
            style = TalangragaTypography.bodySmall,
            modifier = Modifier.constrainAs(dateRef) {
                top.linkTo(usernameRef.bottom, 2.dp)
                start.linkTo(parent.start)
            }
        )

        val amountFormat = amount.formatToIDR()
        Text(
            text = amountFormat,
            style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.constrainAs(amountRef) {
                end.linkTo(parent.end)
                top.linkTo(usernameRef.bottom)
            }
        )
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
                .background(color = Color.White)
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
                text = "Belum ada data transaksi",
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
                text = "Data tabungan, anggota, dan transaksi terakhir akan muncul di sini setelah kamu mulai menambahkan tabungan.",
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
        TransactionEntity(
            transactionId = 1,
            amount = 500000,
            transactionDate = "2025-08-29T22:15:00.000Z",
            statusTransaksi = "",
            reportedDate = "2025-08-29T22:15:00.000Z",
            buktiTransferUrl = "",
            paymentType = "",
            paymentName = "",
            reportedBy = "Iqbal Fauzi",
            confirmedBy = ""
        ),
        TransactionEntity(
            transactionId = 2,
            amount = 250000,
            transactionDate = "2025-08-29T22:15:00.000Z",
            statusTransaksi = "",
            reportedDate = "2025-08-29T22:15:00.000Z",
            buktiTransferUrl = "",
            paymentType = "",
            paymentName = "",
            reportedBy = "Iqbal Fauzi",
            confirmedBy = ""
        ),
        TransactionEntity(
            transactionId = 3,
            amount = 200000,
            transactionDate = "2025-08-29T22:15:00.000Z",
            statusTransaksi = "",
            reportedDate = "2025-08-29T22:15:00.000Z",
            buktiTransferUrl = "",
            paymentType = "",
            paymentName = "",
            reportedBy = "Iqbal Fauzi",
            confirmedBy = ""
        )
    )
    TalangragaTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TransactionSection(transactions = dummyTransactions) {}
            TransactionSection(transactions = emptyList()) {} // Preview for empty state
        }
    }
}

@Preview
@Composable
fun PreviewTransactionItem() {
    TalangragaTheme {
        TransactionItem(
            modifier = Modifier.padding(16.dp),
            username = "Iqbal Fauzi",
            amount = 500000,
            date = "2025-08-29T22:15:00.000Z"
        )
    }
}
