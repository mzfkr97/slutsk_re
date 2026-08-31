package com.romanzhurid.currencies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.currencies.mapper.CurrencyUiMapper
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor

class CurrenciesViewModelFactory(
    private val currenciesInteractor: CurrenciesInteractor,
    private val res: ResourceProvider,
    private val dispatcherProvider: DispatcherProvider,
    private val progressDelegate: ProgressDelegate,
    private val currencyUiMapper: CurrencyUiMapper,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrenciesViewModel(
            currenciesInteractor = currenciesInteractor,
            res = res,
            dispatcherProvider = dispatcherProvider,
            progressDelegate = progressDelegate,
            currencyUiMapper = currencyUiMapper
        ) as T
    }
}