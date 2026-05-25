package com.romanzhurid.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import java.util.UUID

enum class BackStackStrategy {
    ADD,
    REPLACE,
    CLEAR,
}

interface Route : NavKey {
    val backStackStrategy: BackStackStrategy
}

interface FeatureRoute : Route

sealed interface AppRoute : Route {
    val instanceId: String

    @Serializable
    data class Home(
        override val backStackStrategy: BackStackStrategy = BackStackStrategy.ADD,
        override val instanceId: String = UUID.randomUUID().toString(),
    ) : AppRoute
}
