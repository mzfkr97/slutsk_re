package com.romanzhurid.currencies.di

import com.romanzhurid.currencies.ui.CurrenciesViewModelFactory
import org.koin.core.context.GlobalContext

object CurrencyComponentHolder {
    fun getViewModelFactory(): CurrenciesViewModelFactory {
        return GlobalContext.get().get()
    }
}
