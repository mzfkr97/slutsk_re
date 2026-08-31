package com.romanzhurid.common.di

import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.DispatcherProviderImpl
import org.koin.dsl.module

val commonToolsModule = module {
    single<DispatcherProvider> {
        DispatcherProviderImpl()
    }
}
