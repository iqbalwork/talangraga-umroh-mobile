package com.talangraga.umrohmobile.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GradientDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Linear Gradient")
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color.Red, Color.Blue),
                        start = Offset(0f, 0f), // Top-left
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // Bottom-right
                    )
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Radial Gradient")
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Yellow, Color.Green),
                        center = Offset(100.dp.value, 100.dp.value), // Center of the 200.dp Box
                        radius = 100.dp.value, // Radius of the gradient
                        tileMode = TileMode.Clamp // How to fill if gradient doesn't cover the whole area
                    )
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sweep Gradient")
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    Brush.sweepGradient(
                        colors = listOf(Color.Magenta, Color.Cyan),
                        center = Offset(100.dp.value, 100.dp.value) // Center of the sweep
                    )
                )
        )
    }
}

@Preview
@Composable
fun GradientDemoPreview() {
    GradientDemo()
}