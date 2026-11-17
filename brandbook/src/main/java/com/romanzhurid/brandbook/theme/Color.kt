package com.romanzhurid.brandbook.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF2457A6)
val Background = Color(0xFFF4F6FA)
val Surface = Color(0xFFF4F6FA)
val CardSurface = Color(0xFFFFFFFF)
val OnPrimary = Color(0xFFFFFFFF)
val OnBackground = Color(0xFF17243A)
val OnSurface = Color(0xFF17243A)

val Secondary = Color(0xFF1D4E89)
val OnSecondary = Color(0xFFFFFFFF)

val Error = Color(0xFFEF5350)
val OnError = Color(0xFFFFFFFF)

val Outline = Color(0xFF8995A6)
val SurfaceVariant = Color(0xFFE7ECF4)
val OnSurfaceVariant = Color(0xFF526176)

val Accent = Color(0xFF9A6100)

val PrimaryContainer = Color(0xFFE0EAFA)
val OnPrimaryContainer = Color(0xFF17447F)

val SecondaryContainer = Color(0xFFD1E3F5)
val OnSecondaryContainer = Color(0xFF1D4E89)

val ErrorContainer = Color(0xFFFFDAD7)
val OnErrorContainer = Color(0xFF842029)

val SurfaceContainer = Color(0xFFF5F5F5)

val AppLightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary = Accent,
    onTertiary = Color(0xFF000000),

    background = Background,
    onBackground = OnBackground,

    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,

    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,

    outline = Outline,
    outlineVariant = Color(0xFFDEE5EE),
    scrim = Color(0xFF000000),

    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = Color(0xFFEDEDED),
    surfaceContainerLow = Color(0xFFFAFAFA),
)

val DarkPrimary = Color(0xFFACC8FF)
val DarkOnPrimary = Color(0xFF001B3D)

val DarkPrimaryContainer = Color(0xFF1D4E89)
val DarkOnPrimaryContainer = Color(0xFFD6E4F7)

val DarkSecondary = Color(0xFF4A8CDD)
val DarkOnSecondary = Color(0xFF001B2E)

val DarkSecondaryContainer = Color(0xFF1D4E89)
val DarkOnSecondaryContainer = Color(0xFFD1E3F5)

val DarkTertiary = Color(0xFFFFC46B)
val DarkOnTertiary = Color(0xFF3A2F00)

val DarkBackground = Color(0xFF101722)
val DarkOnBackground = Color(0xFFE3E3E3)

val DarkSurface = Color(0xFF101722)
val DarkOnSurface = Color(0xFFE3E3E3)
val DarkCardSurface = Color(0xFF1D2938)

val DarkSurfaceVariant = Color(0xFF303D4F)
val DarkOnSurfaceVariant = Color(0xFFBEC9D9)

val DarkError = Color(0xFFEF5350)
val DarkOnError = Color(0xFF3B0A0A)

val DarkErrorContainer = Color(0xFF842029)
val DarkOnErrorContainer = Color(0xFFFFDAD7)

val DarkOutline = Color(0xFF8491A3)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,

    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,

    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    outline = DarkOutline,
    outlineVariant = Color(0xFF394657),
    scrim = Color(0xFF000000),

    surfaceContainer = Color(0xFF1A1C1F),
    surfaceContainerHigh = Color(0xFF222428),
    surfaceContainerLow = Color(0xFF15171A),
)
