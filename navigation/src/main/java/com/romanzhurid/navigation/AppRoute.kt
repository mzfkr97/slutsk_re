package com.romanzhurid.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface FeatureRoute : NavKey

sealed interface AppRoute : NavKey {
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

    @Serializable
    data class Settings(
        override val instanceId: String = "settings",
    ) : AppRoute

    @Serializable
    data class Cinema(
        override val instanceId: String = "${this.javaClass.canonicalName}",
    ) : AppRoute
}
