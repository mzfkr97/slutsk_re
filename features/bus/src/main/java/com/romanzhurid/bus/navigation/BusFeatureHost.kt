package com.romanzhurid.bus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.bus.di.BusFeatureScope
import com.romanzhurid.bus.ui.BusDetailScreen
import com.romanzhurid.bus.ui.BusDetailViewModel
import com.romanzhurid.bus.ui.BusListScreen
import com.romanzhurid.bus.ui.BusListViewModel
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BusFeatureHost(route: AppRoute.Bus) {
    val busNumber = route.stationId
    val initialRoute = busNumber?.let {
        BusFeatureRoute.BusDetail(busNumber)
    } ?: run {
        BusFeatureRoute.BusList
    }
    FeatureHost(
        route = route,
        featureScope = BusFeatureScope,
        initialStack = {
            listOf(initialRoute)
        },
        entryProviderFactory = { scope, navigator ->
            entryProvider {
                entry<BusFeatureRoute.BusList> {
                    val viewModel = koinViewModel<BusListViewModel>(scope = scope)
                    BusListScreen(
                        viewModel = viewModel,
                        onNavigateToDetail = { busNumber ->
                            navigator.navigate(
                                route = BusFeatureRoute.BusDetail(busNumber)
                            )
                        }
                    )
                }
                entry<BusFeatureRoute.BusDetail> { key ->
                    val viewModel = koinViewModel<BusDetailViewModel>(
                        scope = scope,
                        parameters = { parametersOf(key.busNumber) }
                    )
                    BusDetailScreen(viewModel)
                }
            }
        }
    )
}
