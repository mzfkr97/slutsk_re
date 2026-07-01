package com.romanzhurid.settings.di

import com.romanzhurid.settings.presentation.SettingsViewModelFactory
import dagger.Component

@Component(dependencies = [SettingsComponentDependencies::class])
interface SettingsComponent {
    val settingsViewModelFactory: SettingsViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(dependencies: SettingsComponentDependencies): SettingsComponent
    }
}
