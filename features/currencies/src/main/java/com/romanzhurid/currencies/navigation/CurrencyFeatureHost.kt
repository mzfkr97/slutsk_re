package com.romanzhurid.currencies.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.currencies.di.CurrencyFeatureScope
import com.romanzhurid.currencies.ui.CurrenciesScreen
import com.romanzhurid.currencies.ui.CurrenciesViewModel
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CurrencyFeatureHost(route: AppRoute.Currencies) {

    FeatureHost(
        route = route,
        featureScope = CurrencyFeatureScope,
        initialStack = {
            listOf(CurrencyFeatureRoute.Currencies)
        },
        entryProviderFactory = { scope, navigator ->
            entryProvider {
                entry<CurrencyFeatureRoute.Currencies> {
                    val viewModel = koinViewModel<CurrenciesViewModel>(scope = scope)
                    CurrenciesScreen(viewModel)
                }
            }
        }
    )
}
