package com.romanzhurid.currencies.di

import com.romanzhurid.currencies.ui.CurrenciesViewModelFactory
import com.romanzhurid.currencies.mapper.CurrencyUiMapper
import org.koin.dsl.module

val currenciesModule = module {
    single {
        CurrencyUiMapper(res = get())
    }

    factory {
        CurrenciesViewModelFactory(
            currenciesInteractor = get(),
            res = get(),
            dispatcherProvider = get(),
            progressDelegate = get(),
            currencyUiMapper = get(),
        )
    }
}
