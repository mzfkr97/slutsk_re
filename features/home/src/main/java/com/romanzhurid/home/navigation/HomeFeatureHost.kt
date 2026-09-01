package com.romanzhurid.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.home.di.HomeFeatureScope
import com.romanzhurid.home.presentation.HomeScreen
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost

@Composable
fun HomeFeatureHost(route: AppRoute.Home) {
    FeatureHost(
        route = route,
        featureScope = HomeFeatureScope,
        initialStack = {
            listOf(HomeFeatureRoute.Home)
        },
        entryProviderFactory = { scope ->
            entryProvider<HomeFeatureRoute> {
                entry<HomeFeatureRoute.Home> {
                    HomeScreen()
                }
            }
        }
    )
}