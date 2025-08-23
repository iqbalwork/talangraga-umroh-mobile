package com.talangraga.umrohmobile.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White

// 🌞 Light color scheme
private val LightColors = lightColorScheme(
    primary = Sage,
    secondary = Sandstone,
    background = Porcelain,
    surface = Linen,
    onPrimary = White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

// 🌙 Dark color scheme
private val DarkColors = darkColorScheme(
    primary = SageDark,
    secondary = SandstoneDark,
    background = PorcelainDark,
    surface = LinenDark,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
)

@Composable
expect fun dynamicColorScheme(darkTheme: Boolean): androidx.compose.material3.ColorScheme?

// 🌐 App Theme (works across KMP targets)
@Composable
fun TalangragaTheme(
    darkTheme: Boolean = false,
    useDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colors = if (useDynamicColor) {
        dynamicColorScheme(darkTheme) ?: if (darkTheme) DarkColors else LightColors
    } else {
        if (darkTheme) DarkColors else LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = TalangragaTypography(),
        content = content
    )
}