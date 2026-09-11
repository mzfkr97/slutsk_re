package com.romanzhurid.home.model

sealed interface WeatherState {
    object Loading : WeatherState
    data class Success(val weather: WeatherUi) : WeatherState
    data class Error(
        val message: String,
        val isNoLocation: Boolean
    ) : WeatherState
}