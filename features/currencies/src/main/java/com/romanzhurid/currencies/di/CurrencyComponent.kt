package com.romanzhurid.currencies.di

import com.romanzhurid.currencies.mapper.CurrencyUiMapper
import com.romanzhurid.currencies.ui.CurrenciesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val currenciesModule = module {
    single<CurrencyUiMapper> {
        CurrencyUiMapper(res = get())
    }

    viewModel {
        CurrenciesViewModel(
            currenciesInteractor = get(),
            res = get(),
            dispatcherProvider = get(),
            progressDelegate = get(),
            currencyUiMapper = get<CurrencyUiMapper>(),
        )
    }
}
