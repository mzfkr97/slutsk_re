package com.romanzhurid.navigation.composition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class UiConfig(
    val showBottomBar: Boolean = true,
)

interface UiController {
    fun updateConfig(config: UiConfig)
}

val LocalUiConfig = compositionLocalOf { UiConfig() }

val LocalUiController = staticCompositionLocalOf<UiController> {
    object : UiController {
        override fun updateConfig(config: UiConfig) {}
    }
}

@Composable
fun BindUiConfig(config: UiConfig) {
    val controller = LocalUiController.current
    DisposableEffect(config) {
        controller.updateConfig(config)
        onDispose {
            controller.updateConfig(UiConfig()) // Reset to default
        }
    }
}
