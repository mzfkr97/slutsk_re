package com.romanzhurid.brandbook.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object AppTheme {
    val colorScheme @Composable get() = MaterialTheme.colorScheme
    val typography @Composable get() = MaterialTheme.typography
    val shapes @Composable get() = MaterialTheme.shapes
    val dimensions @Composable get() = LocalDimensions.current
    val localFontsSize: FontsSize
        @Composable
        @ReadOnlyComposable
        get() = LocalFontsSize.current
}
