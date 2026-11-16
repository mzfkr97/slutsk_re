package com.romanzhurid.domain

import kotlinx.coroutines.flow.StateFlow

interface AppSettings {
    var isFirstAppStart: Boolean
    var themeMode: ThemeMode

    val themeModeFlow: StateFlow<ThemeMode>
    val currencyId: Int
}
