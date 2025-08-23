package com.talangraga.umrohmobile.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.inter_bold
import talangragaumrohmobile.composeapp.generated.resources.inter_medium
import talangragaumrohmobile.composeapp.generated.resources.inter_regular
import talangragaumrohmobile.composeapp.generated.resources.inter_semi_bold

@Suppress("ComposableNaming")
@Composable
fun InterFont(): FontFamily {
    return FontFamily(
        Font(
            resource = Res.font.inter_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(resource = Res.font.inter_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.inter_semi_bold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.inter_bold, weight = FontWeight.Bold),
    )
}

@Suppress("ComposableNaming")
@Composable
fun TalangragaTypography() = Typography().run {
    val fontFamily = InterFont()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}