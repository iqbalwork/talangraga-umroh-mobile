package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.umrohmobile.ui.Background
import com.talangraga.umrohmobile.ui.BorderColor
import com.talangraga.umrohmobile.ui.TalangragaTypography
import com.talangraga.umrohmobile.ui.TextSecondaryDark
import com.talangraga.umrohmobile.util.formatIsoTimestampToCustom
import com.talangraga.umrohmobile.util.formatToIDR
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            .background(Background)
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
                style = TalangragaTypography.bodyMedium,
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
            style = TalangragaTypography.titleLarge,
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

