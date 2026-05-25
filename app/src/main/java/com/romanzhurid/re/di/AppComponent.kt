package com.romanzhurid.re.di

import android.content.Context
import com.romanzhurid.common.di.CommonFeatureModule
import com.romanzhurid.common.di.CommonToolsModule
import com.romanzhurid.data.di.CoreDataModule
import com.romanzhurid.home.di.HomeComponentDependencies
import com.romanzhurid.navigation.di.NavigationModule
import com.romanzhurid.re.activity.AppActivity
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
        NavigationModule::class
    ]
)
interface AppComponent :
    HomeComponentDependencies  {

    fun inject(activity: AppActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}
