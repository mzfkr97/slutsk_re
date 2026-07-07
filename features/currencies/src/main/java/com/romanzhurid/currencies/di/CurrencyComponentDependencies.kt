package com.romanzhurid.currencies.di

import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor

interface CurrencyComponentDependencies {
    val currenciesInteractor: CurrenciesInteractor
    val progressDelegate: ProgressDelegate
    val res: ResourceProvider
    val dispatcherProvider: DispatcherProvider
}
