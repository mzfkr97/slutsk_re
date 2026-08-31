package com.romanzhurid.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.home.presentation.HomeScreen
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost

@Composable
fun HomeFeatureHost(route: AppRoute.Home) {
    FeatureHost(
        route = route.instanceId,
        clearComponent = {},
        initialStack = {
            listOf(HomeFeatureRoute.Home)
        },
        entryProviderFactory = {
            entryProvider<HomeFeatureRoute> {
                entry<HomeFeatureRoute.Home> {
                    HomeScreen()
                }
            }
        }
    )
}