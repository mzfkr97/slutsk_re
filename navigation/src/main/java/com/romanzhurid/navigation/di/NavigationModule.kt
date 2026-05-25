package com.romanzhurid.navigation.di

import com.romanzhurid.navigation.featurehost.NavigationChannelProvider
import com.romanzhurid.navigation.featurehost.NavigationChannelProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideNavigationChannelProvider(): NavigationChannelProvider {
        return NavigationChannelProviderImpl()
    }
}
