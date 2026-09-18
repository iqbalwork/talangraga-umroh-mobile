package com.talangraga.umrohmobile.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Multiplatform Window Size Class categories matching Material 3 Adaptive specifications.
 */
enum class WindowWidthSizeClass {
    Compact,   // Phones in portrait (< 600dp)
    Medium,    // Small tablets, foldables unfolded, landscape phones (600dp..839dp)
    Expanded   // Tablets, desktop, wide screens (>= 840dp)
}

/**
 * Calculates current WindowWidthSizeClass based on LocalWindowInfo and LocalDensity.
 */
@Composable
fun rememberWindowWidthSizeClass(): WindowWidthSizeClass {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    return remember(windowInfo.containerSize, density) {
        val widthDp = with(density) { windowInfo.containerSize.width.toDp() }
        when {
            widthDp < 600.dp -> WindowWidthSizeClass.Compact
            widthDp < 840.dp -> WindowWidthSizeClass.Medium
            else -> WindowWidthSizeClass.Expanded
        }
    }
}

/**
 * Determines whether current screen width represents a tablet or wide window (Medium or Expanded).
 */
@Composable
fun isWideScreen(): Boolean {
    val sizeClass = rememberWindowWidthSizeClass()
    return sizeClass != WindowWidthSizeClass.Compact
}

/**
 * Helper to get available width in Dp.
 */
@Composable
fun rememberWindowWidthDp(): Dp {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    return remember(windowInfo.containerSize, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
    }
}
