package com.romanzhurid.settings.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.host.FeatureHost
import com.romanzhurid.settings.di.SettingsComponentHolder
import com.romanzhurid.settings.presentation.SettingsScreen
import com.romanzhurid.settings.presentation.SettingsViewModel

@Composable
fun SettingsFeatureHost(route: AppRoute.Settings) {
    val navigator = LocalNavigator.current

    val entryProvider = entryProvider<SettingsFeatureRoute> {
        entry<SettingsFeatureRoute.Settings> {
            val factory = SettingsComponentHolder.getViewModelFactory()
            val viewModel: SettingsViewModel = viewModel(
                factory = factory
            )

            SettingsScreen(
                viewModel = viewModel,
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
