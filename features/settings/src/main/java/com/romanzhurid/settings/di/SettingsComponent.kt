package com.romanzhurid.settings.di

import com.romanzhurid.settings.presentation.SettingsViewModelFactory
import org.koin.dsl.module

val settingsModule = module {
    factory {
        SettingsViewModelFactory(
            appSettings = get(),
            progressDelegate = get(),
        )
    }
}
