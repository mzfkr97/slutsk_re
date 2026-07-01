package com.romanzhurid.settings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.host.FeatureHost
import com.romanzhurid.settings.di.SettingsComponentDependenciesProvider
import com.romanzhurid.settings.di.SettingsComponentHolder
import com.romanzhurid.settings.presentation.SettingsScreen
import com.romanzhurid.settings.presentation.SettingsViewModel

@Composable
fun SettingsFeatureHost(route: AppRoute.Settings) {
    val context = LocalContext.current.applicationContext
    val component = remember {
        SettingsComponentHolder.get(
            dependencies = (context as SettingsComponentDependenciesProvider)
                .settingsComponentDependencies
        )
    }

    val navigator = LocalNavigator.current

    val entryProvider = remember(component) {
        entryProvider<SettingsFeatureRoute> {
            entry<SettingsFeatureRoute.Settings> {
                val viewModel: SettingsViewModel = viewModel(
                    factory = component.settingsViewModelFactory
                )

                SettingsScreen(
                    viewModel = viewModel,
                    onBackClick = { navigator.back() }
                )
            }
        }
    }

    FeatureHost(
        route = route.instanceId,
        clearComponent = {
            SettingsComponentHolder.clear()
        },
        initialStack = {
            listOf(SettingsFeatureRoute.Settings)
        },
        entryProviderFactory = {
            entryProvider
        }
    )
}
