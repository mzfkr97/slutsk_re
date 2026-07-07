package com.romanzhurid.navigation.composition

import androidx.compose.runtime.staticCompositionLocalOf
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.navigator.Navigator

val LocalNavigator = staticCompositionLocalOf<Navigator<AppRoute>> {
    error("Navigator not provided")
}

