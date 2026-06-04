package com.romanzhurid.home.navigation

import com.romanzhurid.navigation.FeatureRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeFeatureRoute : FeatureRoute {

    @Serializable
    data object Home : HomeFeatureRoute
}
