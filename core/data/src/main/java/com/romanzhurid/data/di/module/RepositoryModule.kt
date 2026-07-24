package com.romanzhurid.data.di.module

import com.romanzhurid.data.repository.cinema.CinemaRepositoryImpl
import com.romanzhurid.data.repository.currencies.CurrencyRepositoryImpl
import com.romanzhurid.domain.cinema.repo.CinemaRepository
import com.romanzhurid.domain.currencies.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsCinemaRepository(impl: CinemaRepositoryImpl): CinemaRepository

    @Binds
    @Singleton
    fun bindsCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository
}
