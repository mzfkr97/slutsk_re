package com.romanzhurid.settings.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.host.FeatureHost
import com.romanzhurid.settings.di.SettingsFeatureScope
import com.romanzhurid.settings.presentation.SettingsScreen
import com.romanzhurid.settings.presentation.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsFeatureHost(route: AppRoute.Settings) {
    val navigator = LocalNavigator.current

    FeatureHost(
        route = route,
        featureScope = SettingsFeatureScope,
        initialStack = {
            listOf(SettingsFeatureRoute.Settings)
        },
        entryProviderFactory = { scope, appNavigator ->
            entryProvider {
                entry<SettingsFeatureRoute.Settings> {
                    val viewModel = koinViewModel<SettingsViewModel>(scope = scope)
                    SettingsScreen(viewModel) {
                        navigator.back()
                    }
                }
            }
        }
    )
}
