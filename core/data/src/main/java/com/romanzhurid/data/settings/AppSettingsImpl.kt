package com.romanzhurid.data.settings

import android.content.SharedPreferences
import com.romanzhurid.data.BuildConfig
import com.romanzhurid.domain.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

class AppSettingsImpl(private val preferences: SharedPreferences) : AppSettings {


    override var isFirstAppStart: Boolean by PreferencesDelegate(
        preferences,
        BuildConfig.PREF_IS_FIRST_APP_START,
        true
    )

    override var isDarkTheme: Boolean
        get() = preferences.getBoolean(BuildConfig.PREF_IS_DARK_THEME, false)
        set(value) {
            preferences.edit { putBoolean(BuildConfig.PREF_IS_DARK_THEME, value) }
            _isDarkThemeFlow.value = value
        }

    private val _isDarkThemeFlow = MutableStateFlow(isDarkTheme)
    override val isDarkThemeFlow: StateFlow<Boolean> = _isDarkThemeFlow.asStateFlow()

    override val currencyId: Int by PreferencesDelegate(
        preferences,
        BuildConfig.PREF_CURRENCY_ID,
        431
    )
}
