package com.talangraga.umrohmobile.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talangraga.shared.Sandstone

@Composable
fun BasicButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier) {
        Text(text = text)
    }
}

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            if (!isLoading) onClick()
        },
        enabled = enabled,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(
                    color = Sandstone,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}


