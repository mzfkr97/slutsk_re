package com.romanzhurid.bus.di

import com.romanzhurid.bus.mapper.BusScheduleUiMapper
import com.romanzhurid.bus.mapper.BusUiMapper
import com.romanzhurid.bus.ui.BusDetailViewModel
import com.romanzhurid.bus.ui.BusListViewModel
import com.romanzhurid.navigation.host.FeatureScope
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

object BusFeatureScope : FeatureScope {
    override val qualifier = named<BusFeatureScope>()
}

val busModule = module {
    scope<BusFeatureScope> {
        scopedOf(::BusUiMapper)
        scopedOf(::BusScheduleUiMapper)
        viewModelOf(::BusListViewModel)
        viewModelOf(::BusDetailViewModel)
    }
}

