package com.talangraga.umrohmobile.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun animateColorSchemeAsState(targetColorScheme: ColorScheme): ColorScheme {
    val primary by animateColorAsState(targetColorScheme.primary)
    val onPrimary by animateColorAsState(targetColorScheme.onPrimary)
    val secondary by animateColorAsState(targetColorScheme.secondary)
    val onSecondary by animateColorAsState(targetColorScheme.onSecondary)
    val background by animateColorAsState(targetColorScheme.background)
    val onBackground by animateColorAsState(targetColorScheme.onBackground)
    val surface by animateColorAsState(targetColorScheme.surface)
    val onSurface by animateColorAsState(targetColorScheme.onSurface)
    val error by animateColorAsState(targetColorScheme.error)
    val onError by animateColorAsState(targetColorScheme.onError)

    return targetColorScheme.copy(
        primary = primary,
        onPrimary = onPrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        error = error,
        onError = onError,
    )
}


// 🌐 App Theme (works across KMP targets)
@Composable
fun TalangragaTheme(
    darkTheme: Boolean = false,
    useDynamicColor: Boolean = true,
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit
) {

    // 1️⃣ Determine the target color scheme
    val targetColorScheme = when {
        colorScheme != null -> colorScheme
        useDynamicColor -> dynamicColorScheme(darkTheme)
            ?: if (darkTheme) DarkColors else LightColors

        else -> if (darkTheme) DarkColors else LightColors
    }

    // 2️⃣ Smoothly animate color transitions
    val animatedColorScheme = animateColorSchemeAsState(targetColorScheme)

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = TalangragaTypography,
        content = content
    )
}
