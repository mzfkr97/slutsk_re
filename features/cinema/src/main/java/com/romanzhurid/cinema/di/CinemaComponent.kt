package com.romanzhurid.cinema.di

import com.romanzhurid.cinema.mapper.CalendarToUiMapper
import com.romanzhurid.cinema.mapper.CinemaToUiMapper
import com.romanzhurid.cinema.ui.CinemaViewModel
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.navigation.host.FeatureScope
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.core.qualifier.named

object CinemaFeatureScope: FeatureScope {
    override val qualifier = named<CinemaFeatureScope>()
}

val cinemaModule = module {

    scope<CinemaFeatureScope> {

        scoped { CalendarToUiMapper() }
        scoped { CinemaToUiMapper() }

        viewModel {
            CinemaViewModel(
                cinemaRepository = get(),
                calendarToUiMapper = get(),
                cinemaMapper = get(),
                dispatcherProvider = get(),
                progressDelegate = get<ProgressDelegate>(),
                networkStateProvider = get(),
            )
        }
    }
}
