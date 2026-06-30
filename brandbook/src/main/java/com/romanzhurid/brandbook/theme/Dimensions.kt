package com.romanzhurid.brandbook.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalDimensions = staticCompositionLocalOf { Dimensions() }

@Stable
data class Dimensions(
    val none: Dp = 0.dp,
    val micro: Dp = 1.dp,
    val xMicro: Dp = 2.dp,
    val xxMicro: Dp = 4.dp,
    val xxxMicro: Dp = 6.dp,
    val small: Dp = 8.dp,
    val xSmall: Dp = 10.dp,
    val xxSmall: Dp = 12.dp,
    val xxxSmall: Dp = 14.dp,
    val medium: Dp = 16.dp,
    val xMedium: Dp = 18.dp,
    val xxMedium: Dp = 20.dp,
    val xxxMedium: Dp = 24.dp,
    val large: Dp = 32.dp,
    val xLarge: Dp = 64.dp,
    val xxLarge: Dp = 64.dp,
    val extraLarge: Dp = 100.dp,
    val huge: Dp = 128.dp,
    val buttonHeightSmall: Dp = 40.dp,
    val buttonHeight: Dp = 48.dp,
    val buttonHeightLarge: Dp = 64.dp,
    val buttonMaxHeightLarge: Dp = 86.dp,
    val appTopAppBarHeight: Dp = 170.dp,
    val videoViewHeight: Dp = 240.dp,
)
