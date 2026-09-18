package com.romanzhurid.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * docs https://openweathermap.org/api/one-call-api
 * https://api.openweathermap.org/data/2.5/onecall?lat=27.56&lon=53.03&exclude=hourly,daily&lang=RU&appid=7893b0fde7d34a64a7706039929369ce
 *
 *
 * {
 *   "coord": {
 *     "lon": 27.5597,
 *     "lat": 53.0274
 *   },
 *   "weather": [
 *     {
 *       "id": 801,
 *       "main": "Clouds",
 *       "description": "небольшая облачность",
 *       "icon": "02d"
 *     }
 *   ],
 *   "base": "stations",
 *   "main": {
 *     "temp": 14.66,
 *     "feels_like": 13.8,
 *     "temp_min": 14.66,
 *     "temp_max": 14.66,
 *     "pressure": 1019,
 *     "humidity": 62,
 *     "sea_level": 1019,
 *     "grnd_level": 1001
 *   },
 *   "visibility": 10000,
 *   "wind": {
 *     "speed": 5.78,
 *     "deg": 281,
 *     "gust": 9.69
 *   },
 *   "clouds": {
 *     "all": 16
 *   },
 *   "dt": 1789108369,
 *   "sys": {
 *     "country": "BY",
 *     "sunrise": 1789097787,
 *     "sunset": 1789144613
 *   },
 *   "timezone": 10800,
 *   "id": 621741,
 *   "name": "Слуцк",
 *   "cod": 200
 * }
 * */

@Serializable
data class OpenWeatherRemote(
    @SerialName("base")
    val base: String? = null,

    @SerialName("clouds")
    val clouds: CloudsRemote? = null,

    @SerialName("cod")
    val cod: Int? = null,

    @SerialName("coord")
    val coord: CoordRemote? = null,

    @SerialName("dt")
    val dt: Long? = null,

    @SerialName("id")
    val id: Int? = null,

    @SerialName("main")
    val main: MainRemote? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("sys")
    val sys: SysRemote? = null,

    @SerialName("timezone")
    val timezone: Int? = null,

    @SerialName("visibility")
    val visibility: Int? = null,

    @SerialName("weather")
    val weather: List<WeatherRemote>? = null,

    @SerialName("wind")
    val wind: WindRemote? = null
) {

    @Serializable
    data class WindRemote(
        @SerialName("deg")
        val deg: Int? = null,

        @SerialName("speed")
        val speed: Float? = null,

        @SerialName("gust")
        val gust: Float? = null
    )

    @Serializable
    data class WeatherRemote(
        @SerialName("description")
        val description: String? = null,

        @SerialName("icon")
        val icon: String? = null,

        @SerialName("id")
        val id: Int? = null,

        @SerialName("main")
        val main: String? = null
    )

    @Serializable
    data class CloudsRemote(
        @SerialName("all")
        val all: Int? = null
    )

    @Serializable
    data class CoordRemote(
        @SerialName("lat")
        val lat: Double? = null,

        @SerialName("lon")
        val lon: Double? = null
    )

    @Serializable
    data class MainRemote(
        @SerialName("feels_like")
        val feelsLike: Double? = null,

        @SerialName("grnd_level")
        val grndLevel: Int? = null,

        @SerialName("humidity")
        val humidity: Int? = null,

        @SerialName("pressure")
        val pressure: Int? = null,

        @SerialName("sea_level")
        val seaLevel: Int? = null,

        @SerialName("temp")
        val temp: Double? = null,

        @SerialName("temp_max")
        val tempMax: Double? = null,

        @SerialName("temp_min")
        val tempMin: Double? = null
    )

    @Serializable
    data class SysRemote(
        @SerialName("country")
        val country: String? = null,

        @SerialName("sunrise")
        val sunrise: Long? = null,

        @SerialName("sunset")
        val sunset: Long? = null
    )
}
