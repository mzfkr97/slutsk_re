package com.romanzhurid.currencies.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.currencies.ui.CurrenciesScreen
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost

@Composable
fun CurrencyFeatureHost(route: AppRoute.Currencies) {
    val entryProvider = entryProvider {
        entry<CurrencyFeatureRoute.Currencies> {
            CurrenciesScreen()
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
