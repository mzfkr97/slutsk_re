package com.romanzhurid.currencies.di

import com.romanzhurid.currencies.mapper.CurrencyUiMapper
import com.romanzhurid.currencies.ui.CurrenciesViewModel
import com.romanzhurid.navigation.host.FeatureScope
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object CurrencyFeatureScope: FeatureScope {
    override val qualifier = named<CurrencyFeatureScope>()
}

val currenciesModule = module {
    scope<CurrencyFeatureScope> {
        scoped { CurrencyUiMapper(res = get()) }

        viewModel {
            CurrenciesViewModel(
                currenciesInteractor = get(),
                res = get(),
                dispatcherProvider = get(),
                progressDelegate = get(),
                currencyUiMapper = get(),
            )
        }
    }
}
