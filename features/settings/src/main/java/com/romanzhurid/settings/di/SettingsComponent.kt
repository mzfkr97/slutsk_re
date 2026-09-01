package com.romanzhurid.settings.di

import com.romanzhurid.navigation.host.FeatureScope
import com.romanzhurid.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object SettingsFeatureScope : FeatureScope {
    override val qualifier = named<SettingsFeatureScope>()
}

val settingsModule = module {
    scope<SettingsFeatureScope> {
        viewModel<SettingsViewModel> {
            SettingsViewModel(
                appSettings = get(),
                progressDelegate = get(),
            )
        }
    }
}
