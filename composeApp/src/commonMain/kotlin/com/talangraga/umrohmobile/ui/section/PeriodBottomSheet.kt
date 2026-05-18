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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.shared.Background
import com.talangraga.shared.Sage
import com.talangraga.shared.currentDate
import com.talangraga.shared.formatDateRange
import com.talangraga.shared.isDateInRange
import com.talangraga.shared.toIndonesianDateFormat
import com.talangraga.umrohmobile.ui.component.InputText
import com.talangraga.umrohmobile.ui.component.LoadingButton
import com.talangraga.umrohmobile.ui.component.TextButtonOption
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddPeriodeSheet(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onDismissRequest: () -> Unit,
    onSubmit: (name: String, startDate: String, endDate: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    var startDateIso by remember { mutableStateOf("") }
    var endDateIso by remember { mutableStateOf("") }

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC).date
                            startDate = date.toIndonesianDateFormat()
                            startDateIso = date.toString()
                        }
                        showStartDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC).date
                            endDate = date.toIndonesianDateFormat()
                            endDateIso = date.toString()
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Background
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tambah Periode Baru",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            InputText(
                title = "Nama Periode",
                value = name,
                onValueChange = { name = it },
                placeholder = "(cth: Bulan ke 38)"
            )

            Column {
                Text(
                    text = "Tanggal Mulai",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextButtonOption(
                    text = startDate,
                    placeholder = "Pilih Tanggal Mulai",
                    trailingIcon = Icons.Default.CalendarToday,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showStartDatePicker = true }
                )
            }

            Column {
                Text(
                    text = "Tanggal Selesai",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextButtonOption(
                    text = endDate,
                    placeholder = "Pilih Tanggal Selesai",
                    trailingIcon = Icons.Default.CalendarToday,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showEndDatePicker = true }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LoadingButton(
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                text = "Simpan Periode",
                enabled = name.isNotBlank() && startDateIso.isNotBlank() && endDateIso.isNotBlank() && !isLoading,
                onClick = {
                    onSubmit(name, startDateIso, endDateIso)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodsSheet(
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
                items = periods.sortedByDescending { it.startDate },
                key = { index, item -> index }
            ) { index, item ->
                val isCurrent = item == selectedPeriod
                val number = Regex("\\d+").find(item.periodeName)?.value?.toIntOrNull()
                PeriodItem(
                    isCurrent = isCurrent,
                    periodNumber = number ?: (index + 1),
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
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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
                    .background(Sage),
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatDateRange(period.startDate, period.endDate),
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
                            .background(Sage.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "SEKARANG",
                            color = Sage,
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
        PeriodEntity(periodId = 0, "Bulan ke 1", "2025-08-06", "2025-09-05"),
        PeriodEntity(1, "Bulan ke 2", "2025-09-06", "2025-10-05"),
        PeriodEntity(2, "Bulan ke 3", "2025-10-06", "2025-11-05"),

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
