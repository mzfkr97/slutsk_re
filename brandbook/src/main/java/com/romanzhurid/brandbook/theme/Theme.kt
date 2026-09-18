package com.romanzhurid.brandbook.theme

import android.app.Activity
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
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
        primary = animateColor(target.primary.toString()) { it.primary }.value,
        onPrimary = animateColor(target.onPrimary.toString()) { it.onPrimary }.value,
        primaryContainer = animateColor(target.primaryContainer.toString()) { it.primaryContainer }.value,
        onPrimaryContainer = animateColor(target.onPrimaryContainer.toString()) { it.onPrimaryContainer }.value,

        secondary = animateColor(target.secondary.toString()) { it.secondary }.value,
        onSecondary = animateColor(target.onSecondary.toString()) { it.onSecondary }.value,

        tertiary = animateColor(target.tertiary.toString()) { it.tertiary }.value,
        onTertiary = animateColor(target.onTertiary.toString()) { it.onTertiary }.value,

        background = animateColor(target.background.toString()) { it.background }.value,
        onBackground = animateColor(target.onBackground.toString()) { it.onBackground }.value,

        surface = animateColor(target.surface.toString()) { it.surface }.value,
        onSurface = animateColor(target.onSurface.toString()) { it.onSurface }.value,

        error = animateColor(target.error.toString()) { it.error }.value,
        onError = animateColor(target.onError.toString()) { it.onError }.value,

        outline = animateColor(target.outline.toString()) { it.outline }.value,
    )
}
