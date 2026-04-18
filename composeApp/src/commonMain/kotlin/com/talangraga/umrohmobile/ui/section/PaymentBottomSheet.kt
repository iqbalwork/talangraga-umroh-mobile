package com.talangraga.umrohmobile.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.shared.Sage
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    scope: CoroutineScope,
    payments: List<PaymentEntity>,
    onBottomSheetChange: (Boolean) -> Unit,
    onChoosePayment: (PaymentEntity) -> Unit
) {
    fun closeSheet() {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onBottomSheetChange(false)
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier.fillMaxSize(),
        onDismissRequest = { onBottomSheetChange(false) },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp)
        ) {
            Text(
                text = "Pilih Metode Pembayaran",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val groupedPayments = payments.groupBy { it.paymentType }
                groupedPayments.forEach { (type, paymentsInType) ->
                    item {
                        Text(
                            text = type,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(paymentsInType) { item ->
                        PaymentItem(
                            payment = item
                        ) {
                            onChoosePayment(it)
                            closeSheet()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentItem(
    payment: PaymentEntity,
    onPaymentClick: (PaymentEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable(onClick = { onPaymentClick(payment) })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for an icon or number
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Sage.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = payment.paymentName.take(1).uppercase(),
                    color = Sage,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = payment.paymentName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = payment.paymentType,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewPaymentItems() {
    val payments = listOf(
        PaymentEntity(paymentId = 1, paymentName = "Transfer Mandiri", paymentType = "Bank Transfer"),
        PaymentEntity(paymentId = 2, paymentName = "BCA", paymentType = "Bank Transfer"),
        PaymentEntity(paymentId = 3, paymentName = "Gopay", paymentType = "E-Wallet")
    )
    TalangragaTheme {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val groupedPayments = payments.groupBy { it.paymentType }
            groupedPayments.forEach { (type, paymentsInType) ->
                item {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(paymentsInType) { item ->
                    PaymentItem(payment = item) {}
                }
            }
        }
    }
}
