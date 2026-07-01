package com.romanzhurid.brandbook.theme

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    isSystemDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val targetColorScheme = when {
        isSystemDarkTheme -> DarkColorScheme
        else -> AppLightColorScheme
    }
    val colorScheme = animateColorScheme(targetColorScheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
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
        primary = animateColor("primary") { it.primary }.value,
        onPrimary = animateColor("onPrimary") { it.onPrimary }.value,
        primaryContainer = animateColor("primaryContainer") { it.primaryContainer }.value,
        onPrimaryContainer = animateColor("onPrimaryContainer") { it.onPrimaryContainer }.value,

        secondary = animateColor("secondary") { it.secondary }.value,
        onSecondary = animateColor("onSecondary") { it.onSecondary }.value,

        tertiary = animateColor("tertiary") { it.tertiary }.value,
        onTertiary = animateColor("onTertiary") { it.onTertiary }.value,

        background = animateColor("background") { it.background }.value,
        onBackground = animateColor("onBackground") { it.onBackground }.value,

        surface = animateColor("surface") { it.surface }.value,
        onSurface = animateColor("onSurface") { it.onSurface }.value,

        error = animateColor("error") { it.error }.value,
        onError = animateColor("onError") { it.onError }.value,

        outline = animateColor("outline") { it.outline }.value,
    )
}