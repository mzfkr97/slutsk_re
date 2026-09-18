package com.romanzhurid.re.application

import android.app.Application
import com.romanzhurid.re.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)

            modules(appModule)
        }
    }
}
