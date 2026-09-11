package com.romanzhurid.domain.weather

data class Weather(
    val description: String,
    val iconCode: String,
    val temperature: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val sunrise: Long,
    val sunset: Long,
    val windSpeed: Float,
    val windDegrees: Int,
    val cityName: String,
)
