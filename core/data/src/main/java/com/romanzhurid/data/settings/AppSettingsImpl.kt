package com.romanzhurid.data.settings

import android.content.SharedPreferences
import com.romanzhurid.domain.AppSettings
import javax.inject.Inject

class AppSettingsImpl @Inject constructor(preferences: SharedPreferences) : AppSettings {

    override var isFirstAppStart: Boolean by PreferencesDelegate(
        preferences,
        "pref_is_first_app_start",
        true
    )
}