package com.romanzhurid.settings.presentation

import androidx.lifecycle.ViewModel
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.AppSettings
import com.romanzhurid.settings.presentation.SettingsViewModel.UiState

class SettingsViewModel(
    private val appSettings: AppSettings,
    progressDelegate: ProgressDelegate
) : ViewModel(),
    UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(
        UiState(
            isDarkTheme = appSettings.isDarkTheme
        )
    ),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val isDarkTheme: Boolean
    )

    fun onThemeChanged(isDark: Boolean) {
        updateUiState { it.copy(isDarkTheme = isDark) }
        appSettings.isDarkTheme = isDark
    }
}
