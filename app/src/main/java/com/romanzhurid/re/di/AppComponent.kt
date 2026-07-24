package com.romanzhurid.re.di

import android.content.Context
import com.romanzhurid.cinema.di.CinemaComponentDependencies
import com.romanzhurid.common.di.CommonFeatureModule
import com.romanzhurid.common.di.CommonToolsModule
import com.romanzhurid.data.di.CoreDataModule
import com.romanzhurid.home.di.HomeComponentDependencies
import com.romanzhurid.currencies.di.CurrencyComponentDependencies
import com.romanzhurid.data.di.module.RepositoryModule
import com.romanzhurid.onboarding.di.OnboardingComponentDependencies
import com.romanzhurid.re.activity.AppActivity
import com.romanzhurid.settings.di.SettingsComponentDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CommonModule::class,
        CommonFeatureModule::class,
        CommonToolsModule::class,
        CoreDataModule::class,
        RepositoryModule::class,
    ]
)
interface AppComponent :
    OnboardingComponentDependencies,
    HomeComponentDependencies,
    CurrencyComponentDependencies,
    SettingsComponentDependencies,
    CinemaComponentDependencies {

    fun inject(activity: AppActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}
