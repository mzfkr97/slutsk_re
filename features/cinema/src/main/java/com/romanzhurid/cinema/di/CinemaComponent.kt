package com.romanzhurid.cinema.di

import com.romanzhurid.cinema.mapper.CalendarToUiMapper
import com.romanzhurid.cinema.mapper.CinemaToUiMapper
import com.romanzhurid.cinema.ui.CinemaViewModel
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val cinemaModule = module {
    single<CalendarToUiMapper> {
        CalendarToUiMapper()
    }

    single<CinemaToUiMapper> {
        CinemaToUiMapper()
    }

    viewModel {
        CinemaViewModel(
            cinemaRepository = get(),
            calendarToUiMapper = get<CalendarToUiMapper>(),
            cinemaMapper = get<CinemaToUiMapper>(),
            dispatcherProvider = get(),
            progressDelegate = get<ProgressDelegate>(),
            networkStateProvider = get(),
        )
    }
}
