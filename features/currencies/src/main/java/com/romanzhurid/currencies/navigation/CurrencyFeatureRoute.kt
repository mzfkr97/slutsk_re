package com.romanzhurid.currencies.navigation

import com.romanzhurid.navigation.FeatureRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface CurrencyFeatureRoute : FeatureRoute {

    @Serializable
    data object Currencies : CurrencyFeatureRoute
}
