package com.romanzhurid.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.home.di.HomeComponentDependenciesProvider
import com.romanzhurid.home.di.HomeComponentHolder
import com.romanzhurid.home.presentation.HomeScreen
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalAppNavigator
import com.romanzhurid.navigation.composition.LocalBackHandler

@Composable
fun HomeFeatureHost(route: AppRoute.Home) {
    val context = LocalContext.current.applicationContext
    val appNavigator = LocalAppNavigator.current
    val parentBack = LocalBackHandler.current

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

    val featureViewModel = viewModel<HomeFeatureHostViewModel>()
    val onBack: () -> Unit = {
        if (featureViewModel.back().not()) {
            parentBack()
        }
    }

    AppNavDisplay(
        backStackFlow = featureViewModel.backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = onBack,
        entryProvider = entryProvider {
            entry<HomeFeatureRoute.Home> {
                HomeScreen()
            }
        }
    )
}
