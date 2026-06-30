package com.romanzhurid.currencies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.currencies.di.CurrencyComponentDependenciesProvider
import com.romanzhurid.currencies.di.CurrencyComponentHolder
import com.romanzhurid.currencies.presentation.CurrenciesScreen
import com.romanzhurid.currencies.presentation.CurrenciesViewModel
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost

@Composable
fun CurrencyFeatureHost(route: AppRoute.Currencies) {
    val context = LocalContext.current.applicationContext

    val component = remember(route.instanceId) {
        CurrencyComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as CurrencyComponentDependenciesProvider)
                .currencyComponentDependencies
        )
    }

    val entryProvider = remember(component) {
        entryProvider {
            entry<CurrencyFeatureRoute.Currencies> {
                val viewModel = viewModel<CurrenciesViewModel>(
                    factory = component.getCurrenciesViewModelFactory()
                )
                CurrenciesScreen(
                    viewModel = viewModel,
                )
            }
        }
    }

    FeatureHost(
        route = route.instanceId,
        clearComponent = {
            CurrencyComponentHolder.clear(route.instanceId)
        },
        initialStack = {
            listOf(CurrencyFeatureRoute.Currencies)
        },
        entryProviderFactory = {
            entryProvider
        }
    )
}
