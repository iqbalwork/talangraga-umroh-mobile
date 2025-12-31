@file:OptIn(ExperimentalMaterial3Api::class)

package com.talangraga.umrohmobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun ModalImagePicker(
    onDismissRequest: () -> Unit,
    onCameraClick: () -> Unit = {},
    onGalleryClick: () -> Unit = {},
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, top = 16.dp),
        ) {
            Text(
                text = "Pilih Sumber Gambar",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCameraClick)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Kamera")
                Text("Kamera")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onGalleryClick)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = "Galeri")
                Text("Galeri")
            }
        }
    }
}

@Preview
@Composable
fun PreviewModalImagePicker() {
    ModalImagePicker(
        onDismissRequest = { }
    )
}
