package com.romanzhurid.re.application

import android.app.Application
import com.romanzhurid.home.di.HomeComponentDependencies
import com.romanzhurid.home.di.HomeComponentDependenciesProvider
import com.romanzhurid.currencies.di.CurrencyComponentDependencies
import com.romanzhurid.currencies.di.CurrencyComponentDependenciesProvider
import com.romanzhurid.re.di.AppComponent
import com.romanzhurid.onboarding.di.OnboardingComponentDependencies
import com.romanzhurid.onboarding.di.OnboardingComponentDependenciesProvider
import com.romanzhurid.re.di.DaggerAppComponent

class App :
    Application(),
    OnboardingComponentDependenciesProvider,
    HomeComponentDependenciesProvider,
    CurrencyComponentDependenciesProvider {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }

    override val onboardingComponentDependencies: OnboardingComponentDependencies
        get() = appComponent

    override val homeComponentDependencies: HomeComponentDependencies
        get() = appComponent

    override val currencyComponentDependencies: CurrencyComponentDependencies
        get() = appComponent
}
