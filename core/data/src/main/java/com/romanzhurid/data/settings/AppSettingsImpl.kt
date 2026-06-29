package com.romanzhurid.data.settings

import android.content.SharedPreferences
import com.romanzhurid.data.BuildConfig
import com.romanzhurid.domain.AppSettings
import javax.inject.Inject

class AppSettingsImpl @Inject constructor(preferences: SharedPreferences) : AppSettings {

    override var isFirstAppStart: Boolean by PreferencesDelegate(
        preferences,
        BuildConfig.PREF_IS_FIRST_APP_START,
        true
    )
}