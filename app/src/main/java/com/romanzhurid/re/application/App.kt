package com.romanzhurid.re.application

import android.app.Application
import com.romanzhurid.re.di.AppComponent
import com.romanzhurid.home.di.HomeComponentDependencies
import com.romanzhurid.home.di.HomeComponentDependenciesProvider
import com.romanzhurid.re.di.DaggerAppComponent

class App :
    Application(),
    HomeComponentDependenciesProvider {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }

    override val homeComponentDependencies: HomeComponentDependencies
        get() = appComponent
}
