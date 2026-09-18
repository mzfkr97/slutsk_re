package com.romanzhurid.domain.currencies.interactor

import com.romanzhurid.domain.weather.Weather
import com.romanzhurid.domain.weather.WeatherRepository

interface WeatherInteractor {
    suspend fun getWeather(lat: Double, lon: Double): Weather
}

class WeatherInteractorImpl(
    private val weatherRepository: WeatherRepository
) : WeatherInteractor {

    override suspend fun getWeather(lat: Double, lon: Double): Weather {
        return weatherRepository.getWeather(lat = lat, lon = lon)
    }
}