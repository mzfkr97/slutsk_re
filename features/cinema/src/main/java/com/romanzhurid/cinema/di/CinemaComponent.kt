package com.romanzhurid.cinema.di

import com.romanzhurid.cinema.mapper.CalendarToUiMapper
import com.romanzhurid.cinema.mapper.CinemaToUiMapper
import com.romanzhurid.cinema.ui.CinemaViewModel
import com.romanzhurid.navigation.host.FeatureScope
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

object CinemaFeatureScope: FeatureScope {
    override val qualifier = named<CinemaFeatureScope>()
}

val cinemaModule = module {

    scope<CinemaFeatureScope> {
        scopedOf(::CalendarToUiMapper)
        scopedOf(::CinemaToUiMapper)
        viewModelOf(::CinemaViewModel)
    }
}
