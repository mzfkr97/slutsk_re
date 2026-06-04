package com.romanzhurid.navigation.composition

import androidx.compose.runtime.staticCompositionLocalOf

val LocalBackHandler = staticCompositionLocalOf<() -> Unit> {
    {
        /* Default: do nothing */
    }
}
