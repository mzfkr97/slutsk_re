package com.romanzhurid.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface Route : NavKey

interface FeatureRoute : Route

sealed interface AppRoute : Route {
    val instanceId: String

    @Serializable
    data class Onboarding(
        override val instanceId: String = "onboarding",
    ) : AppRoute

    @Serializable
    data class Home(
        override val instanceId: String = "home",
    ) : AppRoute

    @Serializable
    data class Currencies(
        override val instanceId: String = "currencies",
    ) : AppRoute
}
