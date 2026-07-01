package com.romanzhurid.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.domain.AppSettings
import javax.inject.Inject

class SettingsViewModelFactory @Inject constructor(
    private val appSettings: AppSettings,
    private val progressDelegate: ProgressDelegate
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            appSettings = appSettings,
            progressDelegate = progressDelegate
        ) as T
    }
}
