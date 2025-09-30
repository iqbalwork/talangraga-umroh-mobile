package com.talangraga.umrohmobile.ui.section

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.umrohmobile.ui.Aqua
import com.talangraga.umrohmobile.ui.Green
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.TalangragaTypography
import com.talangraga.umrohmobile.ui.component.IconBlock
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CardInfoSection(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    notes: String,
    notesColor: Color,
    icon: ImageVector,
    startIconColor: Color,
    endIconColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (textTotalAmountRef, textTotalAmountValueRef, notesRef) = createRefs()
            Text(
                text = title,
                style = TalangragaTypography.titleSmall,
                modifier = Modifier.constrainAs(textTotalAmountRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            Text(
                text = value,
                style = TalangragaTypography.titleMedium,
                modifier = Modifier.constrainAs(textTotalAmountValueRef) {
                    top.linkTo(textTotalAmountRef.bottom, 4.dp)
                    start.linkTo(parent.start)
                }
            )

            Text(
                text = notes,
                style = TalangragaTypography.bodySmall.copy(
                    color = notesColor
                ),
                modifier = Modifier.constrainAs(notesRef) {
                    top.linkTo(textTotalAmountValueRef.bottom, 4.dp)
                    start.linkTo(parent.start)
                }
            )

            IconBlock(
                icon = icon,
                startColor = startIconColor,
                endColor = endIconColor,
                modifier = Modifier.constrainAs(createRef()) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Preview
@Composable
fun CardInfoPreview() {
    TalangragaTheme {
        CardInfoSection(
            modifier = Modifier.fillMaxWidth(),
            title = "Total Tabungan Periode ini",
            value = "RP 72.000.000",
            notes = "12% dari bulan lalu",
            notesColor = Green,
            icon = Icons.Default.AttachMoney,
            startIconColor = Aqua,
            endIconColor = Green
        )
    }
}