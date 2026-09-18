package com.romanzhurid.bus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.bus.di.BusFeatureScope
import com.romanzhurid.bus.ui.BusDetailScreen
import com.romanzhurid.bus.ui.BusDetailViewModel
import com.romanzhurid.bus.ui.BusListScreen
import com.romanzhurid.bus.ui.BusListViewModel
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalBackHandler
import com.romanzhurid.navigation.navigator.NavigatorImpl
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BusFeatureHost(route: AppRoute.Bus) {
    val parentBack = LocalBackHandler.current
    val koin = getKoin()

    val scope = remember(route.instanceId) {
        koin.getOrCreateScope(
            scopeId = route.instanceId,
            qualifier = BusFeatureScope.qualifier
        )
    }

    val navigator = remember(route) {
        val busNumber = route.stationId
        NavigatorImpl(
            initialStack = if (busNumber != null) {
                listOf(BusFeatureRoute.BusList, BusFeatureRoute.BusDetail(busNumber))
            } else {
                listOf(BusFeatureRoute.BusList)
            }
        )
    }

    DisposableEffect(scope) {
        onDispose {
            scope.close()
        }
    }

    val backStack by remember(navigator) {
        derivedStateOf { navigator.backStack.toList() }
    }

    val onBack = remember(navigator, parentBack) {
        {
            navigator.back() || parentBack()
        }
    }

    AppNavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<BusFeatureRoute.BusList> {
                val viewModel = koinViewModel<BusListViewModel>(scope = scope)
                BusListScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { busNumber ->
                        navigator.navigate(BusFeatureRoute.BusDetail(busNumber))
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
    )
}
