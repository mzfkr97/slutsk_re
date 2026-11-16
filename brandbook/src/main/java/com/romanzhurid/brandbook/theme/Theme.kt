package com.romanzhurid.brandbook.theme

import android.app.Activity
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun AppTheme(
    isSystemDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = animateColorScheme(
        target = if (isSystemDarkTheme) {
            DarkColorScheme
        } else {
            AppLightColorScheme
        }
    )
    val cardColor by animateColorAsState(
        targetValue = if (isSystemDarkTheme) DarkCardSurface else Surface,
        animationSpec = tween(durationMillis = 500),
        label = "cardColor"
    )

    val view = LocalView.current

    SideEffect {
        val window = (view.context as Activity).window

        window.statusBarColor = colorScheme.surface.toArgb()
        window.navigationBarColor = colorScheme.surface.toArgb()

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = isSystemDarkTheme.not()
            isAppearanceLightNavigationBars = isSystemDarkTheme.not()
        }
    }

    CompositionLocalProvider(LocalCardColor provides cardColor) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}

@Composable
private fun animateColorScheme(target: ColorScheme): ColorScheme {
    val transition = updateTransition(
        targetState = target,
        label = "themeTransition"
    )

    @Composable
    fun animateColor(
        label: String,
        color: (ColorScheme) -> Color
    ): State<Color> {
        return transition.animateColor(
            transitionSpec = {
                tween(durationMillis = 500)
            },
            label = label
        ) { scheme ->
            color(scheme)
        }
    }

    return target.copy(
        primary = animateColor(target.primary.toString()) { it.primary }.value,
        onPrimary = animateColor(target.onPrimary.toString()) { it.onPrimary }.value,
        primaryContainer = animateColor(target.primaryContainer.toString()) { it.primaryContainer }.value,
        onPrimaryContainer = animateColor(target.onPrimaryContainer.toString()) { it.onPrimaryContainer }.value,
        inversePrimary = animateColor(target.inversePrimary.toString()) { it.inversePrimary }.value,

        secondary = animateColor(target.secondary.toString()) { it.secondary }.value,
        onSecondary = animateColor(target.onSecondary.toString()) { it.onSecondary }.value,
        secondaryContainer = animateColor(target.secondaryContainer.toString()) { it.secondaryContainer }.value,
        onSecondaryContainer = animateColor(target.onSecondaryContainer.toString()) { it.onSecondaryContainer }.value,

        tertiary = animateColor(target.tertiary.toString()) { it.tertiary }.value,
        onTertiary = animateColor(target.onTertiary.toString()) { it.onTertiary }.value,
        tertiaryContainer = animateColor(target.tertiaryContainer.toString()) { it.tertiaryContainer }.value,
        onTertiaryContainer = animateColor(target.onTertiaryContainer.toString()) { it.onTertiaryContainer }.value,

        background = animateColor(target.background.toString()) { it.background }.value,
        onBackground = animateColor(target.onBackground.toString()) { it.onBackground }.value,

        surface = animateColor(target.surface.toString()) { it.surface }.value,
        onSurface = animateColor(target.onSurface.toString()) { it.onSurface }.value,
        surfaceVariant = animateColor(target.surfaceVariant.toString()) { it.surfaceVariant }.value,
        onSurfaceVariant = animateColor(target.onSurfaceVariant.toString()) { it.onSurfaceVariant }.value,
        surfaceTint = animateColor(target.surfaceTint.toString()) { it.surfaceTint }.value,
        inverseSurface = animateColor(target.inverseSurface.toString()) { it.inverseSurface }.value,
        inverseOnSurface = animateColor(target.inverseOnSurface.toString()) { it.inverseOnSurface }.value,
        surfaceBright = animateColor(target.surfaceBright.toString()) { it.surfaceBright }.value,
        surfaceDim = animateColor(target.surfaceDim.toString()) { it.surfaceDim }.value,
        surfaceContainer = animateColor(target.surfaceContainer.toString()) { it.surfaceContainer }.value,
        surfaceContainerHigh = animateColor(target.surfaceContainerHigh.toString()) { it.surfaceContainerHigh }.value,
        surfaceContainerHighest = animateColor(target.surfaceContainerHighest.toString()) { it.surfaceContainerHighest }.value,
        surfaceContainerLow = animateColor(target.surfaceContainerLow.toString()) { it.surfaceContainerLow }.value,
        surfaceContainerLowest = animateColor(target.surfaceContainerLowest.toString()) { it.surfaceContainerLowest }.value,

        error = animateColor(target.error.toString()) { it.error }.value,
        onError = animateColor(target.onError.toString()) { it.onError }.value,
        errorContainer = animateColor(target.errorContainer.toString()) { it.errorContainer }.value,
        onErrorContainer = animateColor(target.onErrorContainer.toString()) { it.onErrorContainer }.value,

        outline = animateColor(target.outline.toString()) { it.outline }.value,
        outlineVariant = animateColor(target.outlineVariant.toString()) { it.outlineVariant }.value,
        scrim = animateColor(target.scrim.toString()) { it.scrim }.value,

        primaryFixed = animateColor(target.primaryFixed.toString()) { it.primaryFixed }.value,
        primaryFixedDim = animateColor(target.primaryFixedDim.toString()) { it.primaryFixedDim }.value,
        onPrimaryFixed = animateColor(target.onPrimaryFixed.toString()) { it.onPrimaryFixed }.value,
        onPrimaryFixedVariant = animateColor(target.onPrimaryFixedVariant.toString()) { it.onPrimaryFixedVariant }.value,
        secondaryFixed = animateColor(target.secondaryFixed.toString()) { it.secondaryFixed }.value,
        secondaryFixedDim = animateColor(target.secondaryFixedDim.toString()) { it.secondaryFixedDim }.value,
        onSecondaryFixed = animateColor(target.onSecondaryFixed.toString()) { it.onSecondaryFixed }.value,
        onSecondaryFixedVariant = animateColor(target.onSecondaryFixedVariant.toString()) { it.onSecondaryFixedVariant }.value,
        tertiaryFixed = animateColor(target.tertiaryFixed.toString()) { it.tertiaryFixed }.value,
        tertiaryFixedDim = animateColor(target.tertiaryFixedDim.toString()) { it.tertiaryFixedDim }.value,
        onTertiaryFixed = animateColor(target.onTertiaryFixed.toString()) { it.onTertiaryFixed }.value,
        onTertiaryFixedVariant = animateColor(target.onTertiaryFixedVariant.toString()) { it.onTertiaryFixedVariant }.value,
    )
}
