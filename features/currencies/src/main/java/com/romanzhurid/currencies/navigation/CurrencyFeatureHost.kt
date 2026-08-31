package com.romanzhurid.currencies.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.currencies.di.CurrencyComponentHolder
import com.romanzhurid.currencies.ui.CurrenciesScreen
import com.romanzhurid.currencies.ui.CurrenciesViewModel
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost

@Composable
fun CurrencyFeatureHost(route: AppRoute.Currencies) {
    val entryProvider = entryProvider {
        entry<CurrencyFeatureRoute.Currencies> {
            val factory = CurrencyComponentHolder.getViewModelFactory()
            val viewModel = viewModel<CurrenciesViewModel>(
                factory = factory
            )
            CurrenciesScreen(viewModel)
        }
    }

    FeatureHost(
        route = route.instanceId,
        clearComponent = {},
        initialStack = {
            listOf(CurrencyFeatureRoute.Currencies)
        },
        entryProviderFactory = {
            entryProvider
        }
    )
}
