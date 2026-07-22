package com.romanzhurid.currencies.di

import com.romanzhurid.currencies.ui.CurrenciesViewModelFactory
import dagger.Component

@Component(dependencies = [CurrencyComponentDependencies::class])
interface CurrencyComponent {

    fun getCurrenciesViewModelFactory(): CurrenciesViewModelFactory

    @Component.Builder
    interface Builder {
        fun currencyComponentDependencies(dependencies: CurrencyComponentDependencies): Builder
        fun build(): CurrencyComponent
    }
}
