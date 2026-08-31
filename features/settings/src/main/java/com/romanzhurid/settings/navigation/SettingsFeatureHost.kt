package com.romanzhurid.settings.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.host.FeatureHost
import com.romanzhurid.settings.presentation.SettingsScreen

@Composable
fun SettingsFeatureHost(route: AppRoute.Settings) {
    val navigator = LocalNavigator.current

    val entryProvider = entryProvider<SettingsFeatureRoute> {
        entry<SettingsFeatureRoute.Settings> {
            SettingsScreen(
                onBackClick = { navigator.back() }
            )
        }
    }

    FeatureHost(
        route = route.instanceId,
        clearComponent = {},
        initialStack = {
            listOf(SettingsFeatureRoute.Settings)
        },
        entryProviderFactory = {
            entryProvider
        }
    )
}
