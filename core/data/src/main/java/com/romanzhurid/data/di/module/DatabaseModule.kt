package com.romanzhurid.data.di.module

import com.romanzhurid.data.local.database.AppDatabase
import com.romanzhurid.data.local.mapper.BusEndPointLocalMapper
import com.romanzhurid.data.local.mapper.BusStationLocalMapper
import com.romanzhurid.data.local.mapper.CurrencyLocalMapper
import com.romanzhurid.data.local.mapper.WeatherRemoteToLocalMapper
import com.romanzhurid.data.remote.cinema.mapper.CinemaRemoteMapper
import com.romanzhurid.data.remote.currencies.mapper.CurrencyRemoteMapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        AppDatabase.getInstance(androidContext())
    }

    factory {
        get<AppDatabase>().currencyDao()
    }

    factory {
        get<AppDatabase>().busStationDao()
    }

    factory {
        get<AppDatabase>().busEndPointDao()
    }

    // Mappers
    single {
        CurrencyLocalMapper()
    }

    single {
        CurrencyRemoteMapper()
    }

    single {
        WeatherRemoteToLocalMapper()
    }

    single {
        BusStationLocalMapper()
    }

    single {
        BusEndPointLocalMapper()
    }

    single {
        CinemaRemoteMapper()
    }
}
