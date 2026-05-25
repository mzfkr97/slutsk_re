package com.romanzhurid.home.navigation

import com.romanzhurid.navigation.BackStackStrategy
import com.romanzhurid.navigation.FeatureRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeFeatureRoute : FeatureRoute {

    @Serializable
    data class Home(
        override val backStackStrategy: BackStackStrategy = BackStackStrategy.ADD,
    ) : HomeFeatureRoute
}
