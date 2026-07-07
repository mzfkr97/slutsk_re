package com.romanzhurid.settings.di

object SettingsComponentHolder {
    private var component: SettingsComponent? = null

    fun get(dependencies: SettingsComponentDependencies): SettingsComponent {
        if (component == null) {
            component = DaggerSettingsComponent.factory().create(dependencies)
        }
        return component!!
    }

    fun clear() {
        component = null
    }
}
