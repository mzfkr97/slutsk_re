package com.romanzhurid.cinema.di

import android.content.Context
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ExceptionsEmitter
import com.romanzhurid.common.NetworkStateFlow
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.domain.cinema.repo.CinemaRepository
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor

interface CinemaComponentDependencies {
    val context: Context
    val currenciesInteractor: CurrenciesInteractor
    val progressDelegate: ProgressDelegate
    val res: ResourceProvider
    val dispatcherProvider: DispatcherProvider
    val cinemaRepository: CinemaRepository
    val exceptionsEmitter: ExceptionsEmitter
    val networkStateFlow: NetworkStateFlow
}
