package com.romanzhurid.navigation.composition

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavKey
import com.romanzhurid.navigation.navigator.Navigator

val LocalFeatureNavigator = staticCompositionLocalOf<Navigator<NavKey>> {
    error("LocalFeatureNavigator not provided")
}
