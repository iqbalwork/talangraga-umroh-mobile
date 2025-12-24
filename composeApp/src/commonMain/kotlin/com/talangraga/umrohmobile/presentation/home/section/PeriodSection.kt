package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.shared.utils.INDONESIA_TRIMMED
import com.talangraga.shared.utils.formatDateRange
import com.talangraga.umrohmobile.ui.component.TextButton
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PeriodSection(
    modifier: Modifier = Modifier,
    period: PeriodEntity?,
    onClickAll: () -> Unit,
    onShowPeriodSheet: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            text = "Semua",
            isSelected = period == null,
            modifier = Modifier
        ) {
            onClickAll()
        }
        Spacer(Modifier.width(16.dp))
        val bulan = if (period != null) {
            formatDateRange(
                period.startDate, period.endDate, monthFormat = INDONESIA_TRIMMED
            )
        } else ""
        TextButtonOption(
            text = bulan,
            placeholder = "Pilih Bulan",
            trailingIcon = Icons.Default.ArrowDropDown,
            modifier = Modifier.fillMaxWidth(),
        ) { onShowPeriodSheet() }
    }
}

@Preview(showBackground = true)
@Composable
fun PeriodSectionPreview() {
    PeriodSection(
        period = null,
        onClickAll = {},
        onShowPeriodSheet = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PeriodSectionWithDataPreview() {
    PeriodSection(
        period = PeriodEntity(1, "Januari", "2023-01-01", "2023-01-31"),
        onClickAll = {},
        onShowPeriodSheet = {}
    )
}
