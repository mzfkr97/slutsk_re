package com.romanzhurid.data.di.module

import com.romanzhurid.data.repository.cinema.CinemaRepositoryImpl
import com.romanzhurid.data.repository.currencies.CurrencyRepositoryImpl
import com.romanzhurid.data.repository.weather.WeatherRepositoryImpl
import com.romanzhurid.data.location.LocationRepositoryImpl
import com.romanzhurid.domain.cinema.repo.CinemaRepository
import com.romanzhurid.domain.location.LocationRepository
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import com.romanzhurid.domain.currencies.interactor.WeatherInteractor
import com.romanzhurid.domain.currencies.interactor.WeatherInteractorImpl
import com.romanzhurid.domain.currencies.repository.CurrencyRepository
import com.romanzhurid.domain.weather.WeatherRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
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
    single<WeatherRepository> {
        WeatherRepositoryImpl(
            weatherApi = get(),
            weatherRemoteToLocalMapper = get(),
        )
    }

    single<LocationRepository> {
        LocationRepositoryImpl(
            context = androidApplication()
        )
    }

    single<WeatherInteractor> {
        WeatherInteractorImpl(
            weatherRepository = get()
        )
    }

    single {
        CurrenciesInteractor(
            repository = get()
        )
    }
}
