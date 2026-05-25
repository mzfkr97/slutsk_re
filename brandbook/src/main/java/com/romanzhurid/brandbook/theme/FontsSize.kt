package com.romanzhurid.brandbook.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

internal val LocalFontsSize = staticCompositionLocalOf { FontsSize() }

@Stable
data class FontsSize(
    val small: TextUnit = 8.sp,
    val xSmall: TextUnit = 10.sp,
    val xxSmall: TextUnit = 12.sp,
    val xxxSmall: TextUnit = 14.sp,
    val medium: TextUnit = 16.sp,
    val xMedium: TextUnit = 20.sp,
    val xxMedium: TextUnit = 24.sp,
    val large: TextUnit = 28.sp,
    val xLarge: TextUnit = 32.sp,
    val xxLarge: TextUnit = 42.sp,
)
