package com.romanzhurid.domain.weather

interface WeatherRepository {

    suspend fun getWeather(lat: Double?, lon: Double?): Weather
}
