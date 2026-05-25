package com.romanzhurid.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.home.di.HomeComponentDependenciesProvider
import com.romanzhurid.home.di.HomeComponentHolder
import com.romanzhurid.home.home.HomeScreen
import com.romanzhurid.home.home.HomeViewModel
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute

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

    DisposableEffect(route.instanceId) {
        onDispose {
            HomeComponentHolder.clear(route.instanceId)
        }
    }

    val factory = component.getHomeFeatureHostViewModelFactory()

    val featureViewModel = viewModel<HomeFeatureHostViewModel>(
        factory = factory,
    )
    val uiState by featureViewModel.collectUiState()

    AppNavDisplay(
        backStack = uiState.backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = featureViewModel::onBack,
        entryProvider = entryProvider {
            entry<HomeFeatureRoute.Home> {
                val viewModel = viewModel<HomeViewModel>(
                    factory = component.getHomeViewModelFactory()
                )
                HomeScreen(
                    viewModel = viewModel,
                    openFeatureRoute = featureViewModel::openFeatureRoute,
                    openAppRoute = featureViewModel::openAppRoute,
                    onBack = featureViewModel::onBack,
                )
            }
        }
    )
}
