package com.romanzhurid.home.di

import com.romanzhurid.common.ExceptionsObserverImpl
import com.romanzhurid.common.ExceptionsEmitter
import com.romanzhurid.common.ProgressObserverImpl
import com.romanzhurid.common.ProgressEmitter
import com.romanzhurid.home.mapper.BusStationsListUiMapper
import com.romanzhurid.home.mapper.WeatherUiMapper
import com.romanzhurid.home.presentation.HomeScreenViewModel
import com.romanzhurid.navigation.host.FeatureScope
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

object HomeFeatureScope : FeatureScope {
    override val qualifier = named<HomeFeatureScope>()
}

val homeModule = module {
    scope<HomeFeatureScope> {
        scoped<ProgressEmitter> { get<ProgressObserverImpl>() }
        scoped<ExceptionsEmitter> { get<ExceptionsObserverImpl>() }
        scopedOf(::ProgressObserverImpl)
        scopedOf(::ExceptionsObserverImpl)
        scopedOf(::WeatherUiMapper)
        scopedOf(::BusStationsListUiMapper)
        viewModelOf(::HomeScreenViewModel)
    }
}
