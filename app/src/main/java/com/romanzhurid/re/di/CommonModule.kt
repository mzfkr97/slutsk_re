package com.romanzhurid.re.di

import com.romanzhurid.cinema.di.cinemaModule
import com.romanzhurid.bus.di.busModule
import com.romanzhurid.common.di.commonFeatureModule
import com.romanzhurid.common.di.commonToolsModule
import com.romanzhurid.common.di.permissionModule
import com.romanzhurid.currencies.di.currenciesModule
import com.romanzhurid.data.di.coreDataModule
import com.romanzhurid.re.activity.mainActivityModule
import com.romanzhurid.settings.di.settingsModule
import com.romanzhurid.onboarding.di.onboardingModule
import com.romanzhurid.home.di.homeModule
import org.koin.dsl.module

val appModule = module {
    includes(
        commonToolsModule,
        permissionModule,
        commonFeatureModule,
        coreDataModule,
        mainActivityModule,
        onboardingModule,
        homeModule,
        currenciesModule,
        settingsModule,
        cinemaModule,
        busModule,
    )
}
