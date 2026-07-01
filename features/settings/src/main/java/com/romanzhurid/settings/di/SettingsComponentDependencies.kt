package com.romanzhurid.settings.di

import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.domain.AppSettings

interface SettingsComponentDependencies {
    val appSettings: AppSettings
    val progressDelegate: ProgressDelegate
}
