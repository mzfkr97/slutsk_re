package com.romanzhurid.currencies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.currencies.di.CurrencyComponentDependenciesProvider
import com.romanzhurid.currencies.di.CurrencyComponentHolder
import com.romanzhurid.currencies.presentation.CurrenciesScreen
import com.romanzhurid.currencies.presentation.CurrenciesViewModel
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalBackHandler
import com.romanzhurid.navigation.navigator.NavigatorImpl

@Composable
fun CurrencyFeatureHost(route: AppRoute.Currencies) {
    val context = LocalContext.current.applicationContext
    val parentBack = LocalBackHandler.current

    val component = remember(route.instanceId) {
        CurrencyComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as CurrencyComponentDependenciesProvider)
                .currencyComponentDependencies
        )
    }

    DisposableEffect(route.instanceId) {
        onDispose {
            CurrencyComponentHolder.clear(route.instanceId)
        }
    }

    val navigator = remember(route.instanceId) {
        NavigatorImpl<CurrencyFeatureRoute>(
            initialStack = listOf(CurrencyFeatureRoute.Currencies)
        )
    }

    val backStack by remember(route.instanceId) {
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

    AppNavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = onBack,
        entryProvider = entryProvider {
            entry<CurrencyFeatureRoute.Currencies> {
                val viewModel = viewModel<CurrenciesViewModel>(
                    factory = component.getCurrenciesViewModelFactory()
                )
                CurrenciesScreen(
                    viewModel = viewModel,
                    onBack = onBack
                )
            }
        }
    )
}
