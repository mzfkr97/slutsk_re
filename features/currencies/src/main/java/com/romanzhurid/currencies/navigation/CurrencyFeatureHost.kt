package com.romanzhurid.currencies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

    val featureViewModel = viewModel<CurrencyFeatureHostViewModel>()
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
