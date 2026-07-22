package com.romanzhurid.common.di

import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.DispatcherProviderImpl
import com.romanzhurid.common.NetworkStateFlow
import com.romanzhurid.common.NetworkStateProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface CommonToolsModule {

    @Binds
    @Singleton
    fun bindsDispatcherProvider(impl: DispatcherProviderImpl): DispatcherProvider
}
