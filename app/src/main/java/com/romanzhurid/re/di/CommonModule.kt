package com.romanzhurid.re.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonModule {

    @Provides
    @ApplicationContext
    @Singleton
    fun provideContext(context: Context): Context = context
}
