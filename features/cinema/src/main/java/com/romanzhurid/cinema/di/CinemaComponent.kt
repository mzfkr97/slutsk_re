package com.romanzhurid.cinema.di

import com.romanzhurid.cinema.mapper.CalendarToUiMapper
import com.romanzhurid.cinema.mapper.CinemaToUiMapper
import com.romanzhurid.cinema.ui.CinemaViewModelFactory
import org.koin.dsl.module

val cinemaModule = module {
    single {
        CalendarToUiMapper()
    }

    single {
        CinemaToUiMapper()
    }

    factory {
        CinemaViewModelFactory(
            cinemaRepository = get(),
            calendarToUiMapper = get(),
            cinemaMapper = get(),
            dispatcherProvider = get(),
            progressDelegate = get(),
            networkStateProvider = get(),
        )
    }
}
