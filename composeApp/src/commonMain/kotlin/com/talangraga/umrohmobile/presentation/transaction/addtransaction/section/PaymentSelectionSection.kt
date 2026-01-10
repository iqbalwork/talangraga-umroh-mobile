package com.talangraga.umrohmobile.presentation.transaction.addtransaction.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.talangraga.shared.Background
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.presentation.transaction.model.PaymentGroupUIData
import com.talangraga.umrohmobile.presentation.transaction.model.PaymentUIData

@Composable
fun PaymentSelectionSection(
    paymentGroups: List<PaymentGroupUIData>,
    onPaymentSelected: (PaymentUIData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Pilih Metode Pembayaran",
            style = TalangragaTypography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(paymentGroups) { group ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = group.paymentType,
                        style = TalangragaTypography.titleSmall.copy(color = Sage),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    group.paymentList.forEach { payment ->
                        PaymentItem(
                            payment = payment,
                            onClick = { onPaymentSelected(payment) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentItem(
    payment: PaymentUIData,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Background),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = payment.paymentName,
                style = TalangragaTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
