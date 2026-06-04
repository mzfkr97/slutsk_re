package com.romanzhurid.navigation.composition

import androidx.compose.runtime.staticCompositionLocalOf
import com.romanzhurid.navigation.navigator.AppNavigator

val LocalAppNavigator = staticCompositionLocalOf<AppNavigator> {
    error("No AppNavigator provided")
}
