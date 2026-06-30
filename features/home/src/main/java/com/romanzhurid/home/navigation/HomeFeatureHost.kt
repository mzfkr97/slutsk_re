package com.romanzhurid.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.home.di.HomeComponentDependenciesProvider
import com.romanzhurid.home.di.HomeComponentHolder
import com.romanzhurid.home.presentation.HomeScreen
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost

@Composable
fun HomeFeatureHost(route: AppRoute.Home) {
    val context = LocalContext.current.applicationContext
    val component = remember(route.instanceId) {
        HomeComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as HomeComponentDependenciesProvider)
                .homeComponentDependencies
        )
    }

    val entryProvider = remember(component) {
        entryProvider<HomeFeatureRoute> {
            entry<HomeFeatureRoute.Home> {
                HomeScreen()
            }
        }
    }

    FeatureHost(
        route = route.instanceId,
        clearComponent = {
            HomeComponentHolder.clear(route.instanceId)
        },
        initialStack = {
            listOf(HomeFeatureRoute.Home)
        },
        entryProviderFactory = {
            entryProvider
        }
    )
}