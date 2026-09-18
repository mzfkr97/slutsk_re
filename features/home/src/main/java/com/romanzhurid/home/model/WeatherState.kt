package com.romanzhurid.home.model

import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState

sealed interface WeatherState {
    object Loading : WeatherState
    data class Success(val weather: WeatherUi) : WeatherState
    data class Error(val message: String, val errorType: ErrorState.ErrorType) : WeatherState
}