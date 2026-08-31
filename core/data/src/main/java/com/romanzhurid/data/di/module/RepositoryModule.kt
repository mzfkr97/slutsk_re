package com.romanzhurid.data.di.module

import com.romanzhurid.data.repository.cinema.CinemaRepositoryImpl
import com.romanzhurid.data.repository.currencies.CurrencyRepositoryImpl
import com.romanzhurid.domain.cinema.repo.CinemaRepository
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import com.romanzhurid.domain.currencies.repository.CurrencyRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<CinemaRepository> {
        CinemaRepositoryImpl(
            apiCinema = get(),
            cinemaRemoteMapper = get()
        )
    }

    single<CurrencyRepository> {
        CurrencyRepositoryImpl(
            api = get(),
            remoteMapper = get(),
            currencyDao = get(),
            localMapper = get()
        )
    }

    single {
        CurrenciesInteractor(
            repository = get()
        )
    }
}
