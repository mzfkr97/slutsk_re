package com.romanzhurid.settings.navigation

import com.romanzhurid.navigation.FeatureRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface SettingsFeatureRoute : FeatureRoute {
    @Serializable
    data object Settings : SettingsFeatureRoute
}
