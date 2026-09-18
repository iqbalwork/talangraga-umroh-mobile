package com.talangraga.umrohmobile.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talangraga.shared.Green
import com.talangraga.shared.Orange
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.formatIsoTimestampToCustom
import com.talangraga.shared.formatToIDR
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    username: String,
    paymentName: String,
    paymentMethod: String,
    amount: Int,
    date: String,
    status: String = "",
    onClick: () -> Unit = {}
) {
    val (statusColor, statusText) = when (status.lowercase()) {
        "completed" -> Green to "Selesai"
        "sent" -> Orange to "Terkirim"
        "on_process" -> Orange to "Diproses"
        else -> MaterialTheme.colorScheme.primary to status.ifBlank { "Terkirim" }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = username,
                    style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "$paymentName - $paymentMethod",
                    style = TalangragaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = date.formatIsoTimestampToCustom(),
                    style = TalangragaTypography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = amount.formatToIDR(),
                    style = TalangragaTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = statusColor.copy(alpha = 0.12f)
                    ),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = statusText,
                        style = TalangragaTypography.bodySmall.copy(
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TransactionItemPreview() {
    TalangragaTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TransactionItem(
                username = "John Doe",
                paymentName = "Bank Transfer",
                paymentMethod = "BCA",
                amount = 100000,
                date = "2023-01-01T12:00:00Z",
                status = "completed"
            )
            TransactionItem(
                username = "Ahmad Syafiq",
                paymentName = "Bank Transfer",
                paymentMethod = "BSI",
                amount = 2500000,
                date = "2023-01-01T12:00:00Z",
                status = "sent"
            )
        }
    }
}
