package com.talangraga.umrohmobile.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.shared.BorderColor
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.TextSecondaryDark
import com.talangraga.shared.formatIsoTimestampToCustom
import com.talangraga.shared.formatToIDR

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    username: String,
    paymentName: String,
    paymentMethod: String,
    amount: Int,
    date: String
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        val (dataRef, amountRef) = createRefs()

        Column(
            modifier = Modifier.constrainAs(dataRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = username,
                style = TalangragaTypography.titleMedium,
                modifier = Modifier
            )

            Text(
                text = "$paymentName - $paymentMethod",
                style = TalangragaTypography.bodySmall,
                modifier = Modifier
            )

            Text(
                text = date.formatIsoTimestampToCustom(),
                style = TalangragaTypography.bodySmall.copy(color = TextSecondaryDark),
                modifier = Modifier
            )
        }

        Text(
            text = amount.formatToIDR(),
            style = TalangragaTypography.titleLarge.copy(color = Sage),
            modifier = Modifier.constrainAs(amountRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}

@Preview
@Composable
fun TransactionItemPreview() {
    TransactionItem(
        username = "John Doe",
        paymentName = "Payment Name",
        paymentMethod = "Payment Method",
        amount = 100000,
        date = "2023-01-01T12:00:00Z"
    )
}

