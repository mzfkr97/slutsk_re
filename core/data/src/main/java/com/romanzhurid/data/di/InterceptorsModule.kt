package com.romanzhurid.data.di

import com.romanzhurid.data.remote.cinema.CinemaAuthInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val interceptorsModule = module {
    single {
        CinemaAuthInterceptor()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}