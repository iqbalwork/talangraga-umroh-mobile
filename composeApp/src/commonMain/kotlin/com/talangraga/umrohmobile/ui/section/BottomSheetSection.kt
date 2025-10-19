package com.talangraga.umrohmobile.ui.section

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogUserType(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    scope: CoroutineScope,
    onBottomSheetChange: (Boolean) -> Unit,
    onChooseUserType: (String) -> Unit
) {

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
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = { onBottomSheetChange(false) }
    ) {
        DropdownMenuItem(
            onClick = {
                onChooseUserType("Admin")
                closeSheet()
            },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Admin",
                        tint = Color(0xFF6C5CE7)
                    )
                    Text(
                        text = "Admin",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            })
        DropdownMenuItem(
            onClick = {
                onChooseUserType("Member")
                closeSheet()
            }, text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Member",
                        tint = Color(0xFF6C5CE7)
                    )
                    Text(
                        text = "Member",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogPeriods(
    modifier: Modifier = Modifier,
    periods: List<PeriodEntity>,
    sheetState: SheetState,
    scope: CoroutineScope,
    onBottomSheetChange: (Boolean) -> Unit,
    onChooseUserType: (String) -> Unit
) {
    fun closeSheet() {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onBottomSheetChange(false)
            }
        }
    }
}