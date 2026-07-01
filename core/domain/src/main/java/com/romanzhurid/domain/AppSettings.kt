package com.romanzhurid.domain

import kotlinx.coroutines.flow.StateFlow

interface AppSettings {
    var isFirstAppStart: Boolean
    var isDarkTheme: Boolean

    val isDarkThemeFlow: StateFlow<Boolean>
}
