package com.romanzhurid.data.di.module

import com.romanzhurid.data.local.mapper.CurrencyLocalMapper
import com.romanzhurid.data.repository.currencies.CurrencyRepositoryImpl
import com.romanzhurid.domain.currencies.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface CurrencyDataModule {

    @Binds
    @Singleton
    fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository

    companion object {
        @Provides
        @Singleton
        fun provideCurrencyLocalMapper(): CurrencyLocalMapper {
            return CurrencyLocalMapper()
        }
    }
}
