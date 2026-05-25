package com.romanzhurid.navigation.featurehost

import com.romanzhurid.navigation.AppRoute

sealed interface NavIntent {
    data class OpenFeature(val appRoute: AppRoute) : NavIntent
    data object Back : NavIntent
}
