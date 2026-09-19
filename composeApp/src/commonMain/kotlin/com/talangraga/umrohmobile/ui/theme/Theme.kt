package com.talangraga.umrohmobile.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import com.talangraga.shared.Background
import com.talangraga.shared.ErrorContainerDark
import com.talangraga.shared.ErrorContainerLight
import com.talangraga.shared.Linen
import com.talangraga.shared.LinenDark
import com.talangraga.shared.MediumPurple
import com.talangraga.shared.OnErrorContainerDark
import com.talangraga.shared.OnErrorContainerLight
import com.talangraga.shared.OnSageContainer
import com.talangraga.shared.OnSageContainerDark
import com.talangraga.shared.OnSandstoneContainer
import com.talangraga.shared.OnSandstoneContainerDark
import com.talangraga.shared.OutlineDark
import com.talangraga.shared.OutlineLight
import com.talangraga.shared.OutlineVariantDark
import com.talangraga.shared.OutlineVariantLight
import com.talangraga.shared.Porcelain
import com.talangraga.shared.PorcelainDark
import com.talangraga.shared.Red
import com.talangraga.shared.Sage
import com.talangraga.shared.SageContainer
import com.talangraga.shared.SageContainerDark
import com.talangraga.shared.SageDark
import com.talangraga.shared.Sandstone
import com.talangraga.shared.SandstoneContainer
import com.talangraga.shared.SandstoneContainerDark
import com.talangraga.shared.SandstoneDark
import com.talangraga.shared.SurfaceContainerDark
import com.talangraga.shared.SurfaceContainerHighDark
import com.talangraga.shared.SurfaceContainerHighLight
import com.talangraga.shared.SurfaceContainerHighestDark
import com.talangraga.shared.SurfaceContainerHighestLight
import com.talangraga.shared.SurfaceContainerLight
import com.talangraga.shared.SurfaceContainerLowDark
import com.talangraga.shared.SurfaceContainerLowLight
import com.talangraga.shared.SurfaceContainerLowestDark
import com.talangraga.shared.SurfaceContainerLowestLight
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.TextOnColor
import com.talangraga.shared.TextSecondaryDark

// 🌞 Light color scheme (Full M3 Token Palette)
private val LightColors = lightColorScheme(
    primary = Sage,
    onPrimary = White,
    primaryContainer = SageContainer,
    onPrimaryContainer = OnSageContainer,
    inversePrimary = SageDark,

    secondary = Sandstone,
    onSecondary = White,
    secondaryContainer = SandstoneContainer,
    onSecondaryContainer = OnSandstoneContainer,

    tertiary = MediumPurple,
    onTertiary = White,
    tertiaryContainer = Color(0xFFDEE0FF),
    onTertiaryContainer = Color(0xFF142071),

    background = Background,
    onBackground = Color(0xFF1A1C1A),

    surface = Porcelain,
    onSurface = Color(0xFF1A1C1A),
    surfaceVariant = Linen,
    onSurfaceVariant = Color(0xFF444843),
    surfaceTint = Sage,
    inverseSurface = Color(0xFF2F312E),
    inverseOnSurface = Color(0xFFF1F1EB),

    surfaceBright = Porcelain,
    surfaceDim = Color(0xFFD9DCD6),
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,

    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    scrim = Black,

    error = Red,
    onError = White,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight
)

// 🌙 Dark color scheme (Full M3 Token Palette)
private val DarkColors = darkColorScheme(
    primary = SageDark,
    onPrimary = Color(0xFF0F2010),
    primaryContainer = SageContainerDark,
    onPrimaryContainer = OnSageContainerDark,
    inversePrimary = Sage,

    secondary = SandstoneDark,
    onSecondary = Color(0xFF291B0F),
    secondaryContainer = SandstoneContainerDark,
    onSecondaryContainer = OnSandstoneContainerDark,

    tertiary = Color(0xFFBAC3FF),
    onTertiary = Color(0xFF1B2778),
    tertiaryContainer = Color(0xFF354297),
    onTertiaryContainer = Color(0xFFDEE0FF),

    background = PorcelainDark,
    onBackground = TextOnColor,

    surface = LinenDark,
    onSurface = TextOnColor,
    surfaceVariant = Color(0xFF2A2E2A),
    onSurfaceVariant = TextSecondaryDark,
    surfaceTint = SageDark,
    inverseSurface = Color(0xFFE2E3DE),
    inverseOnSurface = Color(0xFF1B1C19),

    surfaceBright = Color(0xFF383A38),
    surfaceDim = PorcelainDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,

    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    scrim = Black,

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark
)

@Composable
expect fun dynamicColorScheme(darkTheme: Boolean): ColorScheme?

expect fun isDynamicColorSupported(): Boolean

@Composable
fun animateColorSchemeAsState(targetColorScheme: ColorScheme): ColorScheme {
    val spec = tween<Color>(durationMillis = 350)

    val primary by animateColorAsState(targetColorScheme.primary, animationSpec = spec)
    val onPrimary by animateColorAsState(targetColorScheme.onPrimary, animationSpec = spec)
    val primaryContainer by animateColorAsState(targetColorScheme.primaryContainer, animationSpec = spec)
    val onPrimaryContainer by animateColorAsState(targetColorScheme.onPrimaryContainer, animationSpec = spec)
    val inversePrimary by animateColorAsState(targetColorScheme.inversePrimary, animationSpec = spec)

    val secondary by animateColorAsState(targetColorScheme.secondary, animationSpec = spec)
    val onSecondary by animateColorAsState(targetColorScheme.onSecondary, animationSpec = spec)
    val secondaryContainer by animateColorAsState(targetColorScheme.secondaryContainer, animationSpec = spec)
    val onSecondaryContainer by animateColorAsState(targetColorScheme.onSecondaryContainer, animationSpec = spec)

    val tertiary by animateColorAsState(targetColorScheme.tertiary, animationSpec = spec)
    val onTertiary by animateColorAsState(targetColorScheme.onTertiary, animationSpec = spec)
    val tertiaryContainer by animateColorAsState(targetColorScheme.tertiaryContainer, animationSpec = spec)
    val onTertiaryContainer by animateColorAsState(targetColorScheme.onTertiaryContainer, animationSpec = spec)

    val background by animateColorAsState(targetColorScheme.background, animationSpec = spec)
    val onBackground by animateColorAsState(targetColorScheme.onBackground, animationSpec = spec)

    val surface by animateColorAsState(targetColorScheme.surface, animationSpec = spec)
    val onSurface by animateColorAsState(targetColorScheme.onSurface, animationSpec = spec)
    val surfaceVariant by animateColorAsState(targetColorScheme.surfaceVariant, animationSpec = spec)
    val onSurfaceVariant by animateColorAsState(targetColorScheme.onSurfaceVariant, animationSpec = spec)
    val surfaceTint by animateColorAsState(targetColorScheme.surfaceTint, animationSpec = spec)
    val inverseSurface by animateColorAsState(targetColorScheme.inverseSurface, animationSpec = spec)
    val inverseOnSurface by animateColorAsState(targetColorScheme.inverseOnSurface, animationSpec = spec)

    val surfaceContainerLowest by animateColorAsState(targetColorScheme.surfaceContainerLowest, animationSpec = spec)
    val surfaceContainerLow by animateColorAsState(targetColorScheme.surfaceContainerLow, animationSpec = spec)
    val surfaceContainer by animateColorAsState(targetColorScheme.surfaceContainer, animationSpec = spec)
    val surfaceContainerHigh by animateColorAsState(targetColorScheme.surfaceContainerHigh, animationSpec = spec)
    val surfaceContainerHighest by animateColorAsState(targetColorScheme.surfaceContainerHighest, animationSpec = spec)

    val outline by animateColorAsState(targetColorScheme.outline, animationSpec = spec)
    val outlineVariant by animateColorAsState(targetColorScheme.outlineVariant, animationSpec = spec)

    val error by animateColorAsState(targetColorScheme.error, animationSpec = spec)
    val onError by animateColorAsState(targetColorScheme.onError, animationSpec = spec)
    val errorContainer by animateColorAsState(targetColorScheme.errorContainer, animationSpec = spec)
    val onErrorContainer by animateColorAsState(targetColorScheme.onErrorContainer, animationSpec = spec)

    return targetColorScheme.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        surfaceContainerLowest = surfaceContainerLowest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer
    )
}

// 🌐 App Theme (works across KMP targets with Material You dynamic color support)
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

