package com.romanzhurid.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.AppSettings
import com.romanzhurid.domain.ThemeMode
import com.romanzhurid.settings.presentation.SettingsViewModel.UiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsViewModel(
    private val appSettings: AppSettings,
    progressDelegate: ProgressDelegate
) : ViewModel(),
    UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(
        UiState(
            themeMode = appSettings.themeMode
        )
    ),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val themeMode: ThemeMode
    )

    init {
        appSettings.themeModeFlow
            .onEach { mode -> updateUiState { it.copy(themeMode = mode) } }
            .launchIn(viewModelScope)
    }

    fun onThemeChanged(mode: ThemeMode) {
        appSettings.themeMode = mode
    }
}
