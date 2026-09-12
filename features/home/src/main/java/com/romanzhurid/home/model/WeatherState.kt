package com.romanzhurid.home.model

sealed interface WeatherState {
    object Loading : WeatherState
    data class Success(val weather: WeatherUi) : WeatherState
    data class Error(val message: String, val errorType: ErrorType) : WeatherState {
        enum class ErrorType {
            LOCATION_UNAVAILABLE,
            NO_LOCATION_PERMISSION,
            PERMISSION_PERMANENTLY_DENIED,
            UNKNOWN;

            val shouldRequestPermission: Boolean
                get() = this == NO_LOCATION_PERMISSION
        }
    }
}