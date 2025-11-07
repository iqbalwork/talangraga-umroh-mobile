package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.ui.TalangragaTypography
import com.talangraga.umrohmobile.util.formatDateRange
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PeriodSection(
    modifier: Modifier = Modifier, period: PeriodEntity?, onShowPeriodSheet: () -> Unit
) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .clickable(onClick = onShowPeriodSheet)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = Icons.Filled.EditCalendar, contentDescription = null)
            val dateLabel = if (period?.periodeName.isNullOrBlank()) {
                "-"
            } else {
                "${period.periodeName} : ${
                    formatDateRange(
                        period.startDate, period.endDate
                    )
                }"
            }
            Text(
                text = dateLabel,
                style = TalangragaTypography.bodyMedium,
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
fun PreviewPeriodSection() {
    PeriodSection(
        period = PeriodEntity(
            periodId = 0,
            periodeName = "Ramadhan 2024",
            startDate = "2024-03-10",
            endDate = "2024-04-09",
        ), onShowPeriodSheet = {})
}
