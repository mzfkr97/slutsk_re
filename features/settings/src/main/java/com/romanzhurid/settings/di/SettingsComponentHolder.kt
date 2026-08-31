package com.romanzhurid.settings.di

import com.romanzhurid.settings.presentation.SettingsViewModelFactory
import org.koin.core.context.GlobalContext

object SettingsComponentHolder {
    fun getViewModelFactory(): SettingsViewModelFactory {
        return GlobalContext.get().get()
    }
}
