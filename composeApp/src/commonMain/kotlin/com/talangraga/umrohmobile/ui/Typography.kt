package com.talangraga.umrohmobile.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.inter_bold
import talangragaumrohmobile.composeapp.generated.resources.inter_medium
import talangragaumrohmobile.composeapp.generated.resources.inter_regular
import talangragaumrohmobile.composeapp.generated.resources.inter_semi_bold
import talangragaumrohmobile.composeapp.generated.resources.space_grotesk_bold
import talangragaumrohmobile.composeapp.generated.resources.space_grotesk_medium
import talangragaumrohmobile.composeapp.generated.resources.space_grotesk_regular
import talangragaumrohmobile.composeapp.generated.resources.space_grotesk_semibold

val InterFont
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.inter_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(resource = Res.font.inter_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.inter_semi_bold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.inter_bold, weight = FontWeight.Bold),
    )

val SpaceGroteskFont
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.space_grotesk_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(resource = Res.font.space_grotesk_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.space_grotesk_semibold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.space_grotesk_bold, weight = FontWeight.Bold),
    )

val TalangragaTypography
    @Composable get() = Typography(
        bodyLarge = TextStyle(
            fontFamily = InterFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = InterFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 20.sp
        ),
        titleLarge = TextStyle(
            fontFamily = SpaceGroteskFont,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = SpaceGroteskFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        titleSmall = TextStyle(
            fontFamily = SpaceGroteskFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    )
