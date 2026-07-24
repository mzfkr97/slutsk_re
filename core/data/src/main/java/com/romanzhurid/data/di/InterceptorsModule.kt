package com.romanzhurid.data.di

import com.romanzhurid.data.remote.cinema.CinemaAuthInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class InterceptorsModule {

    @Provides
    @Singleton
    fun provideCinemaAuthInterceptor(): CinemaAuthInterceptor = CinemaAuthInterceptor()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
    }
}