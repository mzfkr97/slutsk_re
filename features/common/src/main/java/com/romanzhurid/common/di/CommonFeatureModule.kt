package com.romanzhurid.common.di

import android.content.Context
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
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonFeatureModule {

    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideExceptionsObserver(): ExceptionsObserverImpl = ExceptionsObserverImpl()

    @Provides
    @Singleton
    fun provideExceptionsFlow(impl: ExceptionsObserverImpl): ExceptionsFlow = impl

    @Provides
    @Singleton
    fun provideExceptionsEmitter(impl: ExceptionsObserverImpl): ExceptionsEmitter = impl

    @Provides
    @Singleton
    fun provideProgressObserver(): ProgressObserverImpl = ProgressObserverImpl()

    @Provides
    @Singleton
    fun provideProgressEmitter(impl: ProgressObserverImpl): ProgressEmitter = impl

    @Provides
    @Singleton
    fun provideProgressFlow(impl: ProgressObserverImpl): ProgressFlow = impl

    @Provides
    @Singleton
    fun provideScmProgressDelegate(impl: ProgressDelegateImpl): ProgressDelegate = impl

    @Provides
    @Singleton
    fun provideNetworkStateProvider(impl: NetworkStateProvider): NetworkStateFlow = impl
}
