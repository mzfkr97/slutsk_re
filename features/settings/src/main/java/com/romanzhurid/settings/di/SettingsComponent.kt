package com.romanzhurid.settings.di

import com.romanzhurid.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel<SettingsViewModel> {
        SettingsViewModel(
            appSettings = get(),
            progressDelegate = get(),
        )
    }
}
