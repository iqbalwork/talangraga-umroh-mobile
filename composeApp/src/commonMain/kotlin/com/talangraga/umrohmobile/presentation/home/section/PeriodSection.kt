package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.shared.utils.INDONESIA_TRIMMED
import com.talangraga.shared.utils.formatDateRange
import com.talangraga.umrohmobile.ui.component.TextButton
import com.talangraga.umrohmobile.ui.component.TextButtonOption

@Composable
fun PeriodSection(
    modifier: Modifier = Modifier,
    period: PeriodEntity?,
    onClickAll: () -> Unit,
    onShowPeriodSheet: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
    ) {
        val (allRef, periodRef) = createRefs()
        TextButton(
            text = "Semua",
            isSelected = period == null,
            modifier = Modifier.constrainAs(allRef) {
                top.linkTo(periodRef.top)
                bottom.linkTo(periodRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(periodRef.start)
            }
        ) {
            onClickAll()
        }

        val bulan = period?.let {
            formatDateRange(
                it.startDate, it.endDate, monthFormat = INDONESIA_TRIMMED
            )
        }
        TextButtonOption(
            text = bulan.orEmpty(),
            placeholder = "Pilih Bulan",
            trailingIcon = Icons.Default.ArrowDropDown,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(periodRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(allRef.end, 12.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
        ) { onShowPeriodSheet() }
    }
}
