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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.ui.BackgroundColor
import com.talangraga.umrohmobile.ui.PeriodColor
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.TargetColor
import com.talangraga.umrohmobile.ui.TextBodyColor
import com.talangraga.umrohmobile.util.currentDate
import com.talangraga.umrohmobile.util.formatDateRange
import com.talangraga.umrohmobile.util.isDateInRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogPeriods(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    scope: CoroutineScope,
    periods: List<PeriodEntity>,
    onBottomSheetChange: (Boolean) -> Unit,
    onChoosePeriod: (PeriodEntity) -> Unit
) {
    val selectedPeriod = periods.find { data ->
        currentDate.isDateInRange(data.startDate, data.endDate)
    } ?: periods.firstOrNull()

    val listState = rememberLazyListState()
    val selectedIndex = periods.indexOf(selectedPeriod).takeIf { it != -1 }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedIndex) {
        if (selectedIndex != null) {
            coroutineScope.launch {
                listState.animateScrollToItem(selectedIndex)
            }
        }
    }

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
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            itemsIndexed(
                items = periods,
                key = { index, item -> index }
            ) { index, item ->
                val isCurrent = item == selectedPeriod
                PeriodItem(
                    isCurrent = isCurrent,
                    periodNumber = index + 1,
                    period = item
                ) {
                    onChoosePeriod(it)
                    closeSheet()
                }
            }
        }
    }
}

@Composable
fun PeriodItem(
    isCurrent: Boolean = false,
    periodNumber: Int,
    period: PeriodEntity,
    onPeriodClick: (PeriodEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundColor)
            .clickable(onClick = {
                onPeriodClick(period)
            })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Number container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PeriodColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = periodNumber.toString(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Text content
            Column(
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            ) {
                Text(
                    text = period.periodeName,
                    color = TextBodyColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatDateRange(period.startDate, period.endDate),
                    color = TargetColor,
                    fontSize = 14.sp
                )
            }

            // Status or Target/Member count
            Column(
                horizontalAlignment = Alignment.End
            ) {
                if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Green.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "CURRENT",
                            color = Color.Green,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPeriodItems() {
    val periods = listOf(
        PeriodEntity(periodId = 0,  "Bulan ke 1", "2025-08-06", "2025-09-05"),
        PeriodEntity(1,  "Bulan ke 2", "2025-09-06", "2025-10-05"),
        PeriodEntity(2,  "Bulan ke 3", "2025-10-06", "2025-11-05"),

        )
    TalangragaTheme {
        LazyColumn {
            itemsIndexed(
                items = periods,
                key = { index, item -> index }
            ) { index, item ->
                PeriodItem(
                    isCurrent = index == 0,
                    periodNumber = index + 1,
                    period = item
                ) {

                }
            }
        }
    }
}
