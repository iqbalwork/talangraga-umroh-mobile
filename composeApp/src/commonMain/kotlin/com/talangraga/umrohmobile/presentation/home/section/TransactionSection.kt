package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.umrohmobile.ui.Green
import com.talangraga.umrohmobile.ui.InterFont
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.UmrohMobileTypography
import com.talangraga.umrohmobile.util.formatIsoTimestampToCustom
import com.talangraga.umrohmobile.util.formatToIDR
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun TransactionItem(modifier: Modifier = Modifier, username: String, amount: Int, date: String) {
    val fontFamily = InterFont()
    ConstraintLayout(
        modifier = modifier
            .background(color = Color.White)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val (usernameRef, amountRef, dateRef) = createRefs()

        Text(
            text = username,
            style = UmrohMobileTypography(fontFamily).title.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.constrainAs(usernameRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )

        val dateFormat = date.formatIsoTimestampToCustom()
        Text(
            text = dateFormat,
            style = UmrohMobileTypography(fontFamily).basicTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.constrainAs(dateRef) {
                top.linkTo(usernameRef.bottom, 4.dp)
                start.linkTo(parent.start)
            }
        )

        val amountFormat = amount.formatToIDR()
        Text(
            text = amountFormat,
            style = UmrohMobileTypography(fontFamily).title.copy(
                color = Green,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.constrainAs(amountRef) {
                top.linkTo(usernameRef.bottom, 4.dp)
                end.linkTo(parent.end)
            }
        )
    }

}

@Preview
@Composable
fun PreviewTransactionItem(modifier: Modifier = Modifier) {
    TalangragaTheme {
        TransactionItem(modifier, "Iqbal Fauzi", 500000, "2025-08-29T22:15:00.000Z")
    }
}