package com.romanzhurid.data.repository.weather

import com.romanzhurid.data.BuildConfig
import com.romanzhurid.data.local.mapper.WeatherRemoteToLocalMapper
import com.romanzhurid.data.remote.weather.WeatherApi
import com.romanzhurid.domain.weather.Weather
import com.romanzhurid.domain.weather.WeatherRepository

class WeatherRepositoryImpl (
    private val weatherApi: WeatherApi,
    private val weatherRemoteToLocalMapper: WeatherRemoteToLocalMapper
) : WeatherRepository {

    companion object {
        private const val CITY_ID = "621741"
        private const val LANG = "ru"
        private const val UNITS = "metric"
    }

    override suspend fun getWeather(lat: Double?, lon: Double?): Weather {
        val remote = if (lat != null && lon != null) {
            weatherApi.getWeatherByCoordinates(
                latitude = lat,
                longitude = lon,
                units = UNITS,
                language = LANG,
                apiKey = BuildConfig.KEY_WEATHER_API
            )
        } else {
            weatherApi.getWeather(
                id = CITY_ID,
                lang = LANG,
                units = UNITS,
                appId = BuildConfig.KEY_WEATHER_API
            )
        }

        return weatherRemoteToLocalMapper.map(remote)
    }
}