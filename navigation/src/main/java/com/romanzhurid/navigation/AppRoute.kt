package com.romanzhurid.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import java.util.UUID

interface Route : NavKey

interface FeatureRoute : Route

sealed interface AppRoute : Route {
    val instanceId: String

    @Serializable
    data class Onboarding(
        override val instanceId: String = UUID.randomUUID().toString(),
    ) : AppRoute
}
