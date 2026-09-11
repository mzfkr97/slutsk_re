package com.romanzhurid.data.local.mapper

import com.romanzhurid.data.remote.model.OpenWeatherRemote
import com.romanzhurid.domain.weather.Weather

class WeatherRemoteToLocalMapper {

    fun map(model: OpenWeatherRemote): Weather {
        return with(model) {
            Weather(
                description = weather?.firstOrNull()?.description.orEmpty(),
                iconCode = weather?.firstOrNull()?.icon.orEmpty(),
                temperature = main?.temp ?: 0.0,
                feelsLike = main?.feelsLike ?: 0.0,
                pressure = main?.pressure ?: 0,
                humidity = main?.humidity ?: 0,
                sunrise = sys?.sunrise ?: 0L,
                sunset = sys?.sunset ?: 0L,
                windSpeed = wind?.speed ?: 0f,
                windDegrees = wind?.deg ?: 0,
                cityName = name.orEmpty(),
            )
        }
    }
}
