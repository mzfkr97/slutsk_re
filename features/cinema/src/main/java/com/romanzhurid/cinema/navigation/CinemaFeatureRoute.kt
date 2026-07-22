package com.romanzhurid.cinema.navigation

import com.romanzhurid.navigation.FeatureRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface CinemaFeatureRoute : FeatureRoute {

    @Serializable
    data object Cinema : CinemaFeatureRoute

    @Serializable
    data object CinemaDetails : CinemaFeatureRoute

    @Serializable
    data object CinemaGallery : CinemaFeatureRoute
}
