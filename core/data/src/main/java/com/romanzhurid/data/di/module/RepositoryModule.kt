package com.romanzhurid.data.di.module

import com.romanzhurid.data.repository.bus.BusEndPointRepositoryImpl
import com.romanzhurid.data.repository.bus.BusScheduleRepositoryImpl
import com.romanzhurid.data.repository.bus.BusStationRepositoryImpl
import com.romanzhurid.data.repository.cinema.CinemaRepositoryImpl
import com.romanzhurid.data.repository.currencies.CurrencyRepositoryImpl
import com.romanzhurid.data.repository.weather.WeatherRepositoryImpl
import com.romanzhurid.data.location.LocationRepositoryImpl
import com.romanzhurid.domain.bus.interactor.BusEndPointInteractor
import com.romanzhurid.domain.bus.interactor.BusScheduleInteractor
import com.romanzhurid.domain.bus.interactor.BusStationInteractor
import com.romanzhurid.domain.bus.repository.BusEndPointRepository
import com.romanzhurid.domain.bus.repository.BusScheduleRepository
import com.romanzhurid.domain.bus.repository.BusStationRepository
import com.romanzhurid.domain.cinema.repo.CinemaRepository
import com.romanzhurid.domain.location.LocationRepository
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import com.romanzhurid.domain.currencies.interactor.WeatherInteractor
import com.romanzhurid.domain.currencies.interactor.WeatherInteractorImpl
import com.romanzhurid.domain.currencies.repository.CurrencyRepository
import com.romanzhurid.domain.weather.WeatherRepository
import org.koin.android.ext.koin.androidApplication
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
            localMapper = get(),
            appSettings = get(),
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

    single<BusStationRepository> {
        BusStationRepositoryImpl(
            busStationDao = get(),
            mapper = get()
        )
    }

    single {
        BusStationInteractor(
            repository = get()
        )
    }

    single<BusEndPointRepository> {
        BusEndPointRepositoryImpl(
            busEndPointDao = get(),
            mapper = get()
        )
    }

    single {
        BusEndPointInteractor(
            repository = get()
        )
    }

    single<BusScheduleRepository> {
        BusScheduleRepositoryImpl(
            busScheduleDao = get(),
            mapper = get()
        )
    }

    single {
        BusScheduleInteractor(
            repository = get()
        )
    }
}
