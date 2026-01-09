package com.talangraga.umrohmobile.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.talangraga.shared.TalangragaTypography

sealed class ToastType(
    val backgroundColor: Color,
    val borderColor: Color,
    val iconBackgroundColor: Color,
    val iconColor: Color,
    val icon: ImageVector
) {
    data object Success : ToastType(
        backgroundColor = Color(0xFFF0FFF4), // Light Green
        borderColor = Color(0xFF48BB78), // Green
        iconBackgroundColor = Color(0xFF48BB78),
        iconColor = Color.White,
        icon = Icons.Default.Check
    )

    data object Info : ToastType(
        backgroundColor = Color(0xFFF0F5FF), // Light Blue
        borderColor = Color(0xFF4299E1), // Blue
        iconBackgroundColor = Color(0xFF4299E1),
        iconColor = Color.White,
        icon = Icons.Default.Info
    )

    data object Warning : ToastType(
        backgroundColor = Color(0xFFFFFFF0), // Light Yellow
        borderColor = Color(0xFFECC94B), // Yellow
        iconBackgroundColor = Color(0xFFECC94B),
        iconColor = Color.White,
        icon = Icons.Default.PriorityHigh
    )

    data object Error : ToastType(
        backgroundColor = Color(0xFFFFF5F5), // Light Red
        borderColor = Color(0xFFF56565), // Red
        iconBackgroundColor = Color(0xFFF56565),
        iconColor = Color.White,
        icon = Icons.Default.Close
    )
}

@Composable
fun ToastComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String,
    type: ToastType,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(type.backgroundColor)
            .border(1.dp, type.borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(type.iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = type.icon,
                contentDescription = null,
                tint = type.iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Text Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (!title.isNullOrBlank()) {
                Text(
                    text = title,
                    style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }
            Text(
                text = message,
                style = TalangragaTypography.bodyMedium,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Action Button
        if (!actionButtonText.isNullOrBlank() && onActionClick != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable { onActionClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = actionButtonText,
                    style = TalangragaTypography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

        // Close Button
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.Gray,
            modifier = Modifier
                .size(20.dp)
                .clickable { onDismiss() }
        )
    }
}
