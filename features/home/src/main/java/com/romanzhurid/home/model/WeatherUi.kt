package com.romanzhurid.home.model

data class WeatherUi(
    val description: String,
    val iconCode: String,
    val temperature: String,
    val feelsLike: String,
    val pressure: String,
    val humidity: String,
    val sunrise: String,
    val sunset: String,
    val windSpeed: String,
    val windDegrees: String,
    val cityName: String,
    val currentDate: String,
)