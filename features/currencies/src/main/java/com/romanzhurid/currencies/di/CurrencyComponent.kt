package com.romanzhurid.currencies.di

import com.romanzhurid.currencies.mapper.CurrencyUiMapper
import com.romanzhurid.currencies.ui.CurrenciesViewModel
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import com.romanzhurid.navigation.host.FeatureScope
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

object CurrencyFeatureScope: FeatureScope {
    override val qualifier = named<CurrencyFeatureScope>()
}

val currenciesModule = module {
    scope<CurrencyFeatureScope> {
        scopedOf(::CurrencyUiMapper)
        scopedOf(::CurrenciesInteractor)
        viewModelOf(::CurrenciesViewModel)
    }
}
