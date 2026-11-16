package com.romanzhurid.data.settings

import android.content.SharedPreferences
import com.romanzhurid.data.BuildConfig
import com.romanzhurid.domain.AppSettings
import com.romanzhurid.domain.ThemeMode
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

    override var themeMode: ThemeMode
        get() {
            val savedMode = preferences.getString(BuildConfig.PREF_THEME_MODE, null)
            return when {
                savedMode != null -> ThemeMode.valueOf(savedMode)
                preferences.contains(BuildConfig.PREF_IS_DARK_THEME) ->
                    if (preferences.getBoolean(BuildConfig.PREF_IS_DARK_THEME, false)) {
                        ThemeMode.DARK
                    } else {
                        ThemeMode.LIGHT
                    }
                else -> ThemeMode.SYSTEM
            }
        }
        set(value) {
            preferences.edit {
                putString(BuildConfig.PREF_THEME_MODE, value.name)
                remove(BuildConfig.PREF_IS_DARK_THEME)
            }
            _themeModeFlow.value = value
        }

    private val _themeModeFlow = MutableStateFlow(themeMode)
    override val themeModeFlow: StateFlow<ThemeMode> = _themeModeFlow.asStateFlow()

    override var currencyId: Int
        get() = preferences.getInt(BuildConfig.PREF_CURRENCY_ID, 431)
        set(value) {
            preferences.edit { putInt(BuildConfig.PREF_CURRENCY_ID, value) }
            _currencyIdFlow.value = value
        }

    private val _currencyIdFlow = MutableStateFlow(currencyId)
    override val currencyIdFlow: StateFlow<Int> = _currencyIdFlow.asStateFlow()
}
