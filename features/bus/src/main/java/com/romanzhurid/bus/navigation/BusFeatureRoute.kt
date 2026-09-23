package com.romanzhurid.bus.navigation

import com.romanzhurid.navigation.FeatureRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface BusFeatureRoute : FeatureRoute {

    @Serializable
    data object BusList : BusFeatureRoute

    @Serializable
    data class BusDetail(val busNumber: Int) : BusFeatureRoute
}
