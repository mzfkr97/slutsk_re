package com.romanzhurid.common.di

import com.romanzhurid.common.ExceptionsEmitter
import com.romanzhurid.common.ExceptionsFlow
import com.romanzhurid.common.ExceptionsObserverImpl
import com.romanzhurid.common.NetworkStateFlow
import com.romanzhurid.common.NetworkStateProvider
import com.romanzhurid.common.ProgressEmitter
import com.romanzhurid.common.ProgressFlow
import com.romanzhurid.common.ProgressObserverImpl
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.ResourceProviderImpl
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.progressdelegate.ProgressDelegateImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonFeatureModule = module {
    single<ResourceProvider> {
        ResourceProviderImpl(androidContext())
    }

    single<ExceptionsObserverImpl> {
        ExceptionsObserverImpl()
    }

    single<ProgressObserverImpl> {
        ProgressObserverImpl()
    }

    single<ProgressDelegate> {
        ProgressDelegateImpl(
            progressEmitter = get<ProgressObserverImpl>(),
            exceptionsEmitter = get<ExceptionsObserverImpl>()
        )
    }

    single<NetworkStateProvider> {
        NetworkStateProvider(
            context = androidContext()
        )
    }

    single<NetworkStateFlow> {
        get<NetworkStateProvider>()
    }
}
