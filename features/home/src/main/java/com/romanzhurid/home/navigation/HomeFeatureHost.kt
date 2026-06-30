package com.romanzhurid.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.home.di.HomeComponentDependenciesProvider
import com.romanzhurid.home.di.HomeComponentHolder
import com.romanzhurid.home.presentation.HomeScreen
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalBackHandler
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.navigator.NavigatorImpl

@Composable
fun HomeFeatureHost(route: AppRoute.Home) {
    val context = LocalContext.current.applicationContext
    val parentBack = LocalBackHandler.current

    val component = remember(route.instanceId) {
        HomeComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as HomeComponentDependenciesProvider)
                .homeComponentDependencies
        )
    }

    val navigator = remember(route.instanceId) {
        NavigatorImpl<HomeFeatureRoute>(
            initialStack = listOf(HomeFeatureRoute.Home)
        )
    }

    DisposableEffect(route.instanceId) {
        onDispose {
            HomeComponentHolder.clear(route.instanceId)
        }
    }

    val backStack by remember(navigator) {
        derivedStateOf { navigator.backStack.toList() }
    }


    val onBack = remember(navigator, parentBack) {
        {
            if (navigator.back()) {
                true
            } else {
                parentBack()
            }
        }
    }

    val entryProvider = remember(component) {
        entryProvider<HomeFeatureRoute> {
            entry<HomeFeatureRoute.Home> {
                HomeScreen()
            }
        }
    }

    AppNavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider
    )
}