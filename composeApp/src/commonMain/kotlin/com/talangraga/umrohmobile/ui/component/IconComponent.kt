package com.talangraga.umrohmobile.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.talangraga.shared.Aqua
import com.talangraga.shared.Green

@Composable
fun IconBlock(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    startColor: Color,
    endColor: Color,
    size: Dp = 50.dp,
    iconSize: Dp = 30.dp
) {
    val gradientColors = listOf(startColor, endColor)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(gradientColors)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Dollar sign",
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Preview
@Composable
fun PreviewDollarSignIconComponent() {
    IconBlock(
        icon = Icons.Default.AttachMoney,
        startColor = Aqua,
        endColor = Green
    )
}
