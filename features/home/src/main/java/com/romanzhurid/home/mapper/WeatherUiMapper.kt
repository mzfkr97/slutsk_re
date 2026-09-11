package com.romanzhurid.home.mapper

import com.romanzhurid.brandbook.R
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.ext.DatePattern
import com.romanzhurid.common.ext.DatePattern.PATTERN__DD__MMMM_EEE
import com.romanzhurid.domain.weather.Weather
import com.romanzhurid.home.model.WeatherUi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class WeatherUiMapper(private val res: ResourceProvider) {

    companion object {
        private const val WEATHER_ICON_URL_TEMPLATE = "https://openweathermap.org/img/wn/%s@4x.png"
    }

    fun map(model: Weather): WeatherUi {
        return WeatherUi(
            description = model.description.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase()
                else char.toString()
            },
            iconCode = WEATHER_ICON_URL_TEMPLATE.format(model.iconCode),
            temperature = res.getString(R.string.weather__temperature, model.temperature.roundToInt().toString()),
            feelsLike = res.getString(R.string.weather__feels_like, model.feelsLike.roundToInt().toString()),
            pressure = model.pressure.calculatePressure(),
            humidity = model.humidity.toString(),
            sunrise = model.sunrise.toTime(),
            sunset = model.sunset.toTime(),
            windSpeed = model.windSpeed.calculateWindSpeed(),
            windDegrees = model.windDegrees.calculateDegreesToWindDirection(),
            cityName = model.cityName,
            currentDate = DateTimeFormatter
                .ofPattern(PATTERN__DD__MMMM_EEE)
                .format(LocalDate.now()),
        )
    }

    private fun Float.calculateWindSpeed(): String {
        val windSpeed = (this * 3.6).toInt()
        return res.getString(R.string.weather__wind_speed, windSpeed, this)
    }

    private fun Int.calculatePressure(): String {
        val pressure = (this * 0.75006).toInt()
        return res.getString(R.string.weather__pressure, pressure, this)
    }

    private fun Long.toTime(): String {
        return Instant
            .ofEpochSecond(this)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(DateTimeFormatter.ofPattern(DatePattern.PATTERN__HH_MM))
    }

    private fun Int.calculateDegreesToWindDirection(): String {
        val resId = when (this) {
            in 349..360, in 0..11 -> R.string.weather__wind_direction_north
            in 12..33 -> R.string.weather__wind_direction_nne
            in 34..56 -> R.string.weather__wind_direction_ne
            in 57..78 -> R.string.weather__wind_direction_ene
            in 79..101 -> R.string.weather__wind_direction_east
            in 102..123 -> R.string.weather__wind_direction_ese
            in 124..146 -> R.string.weather__wind_direction_se
            in 147..168 -> R.string.weather__wind_direction_sse
            in 169..191 -> R.string.weather__wind_direction_south
            in 192..213 -> R.string.weather__wind_direction_ssw
            in 214..236 -> R.string.weather__wind_direction_sw
            in 237..258 -> R.string.weather__wind_direction_wsw
            in 259..281 -> R.string.weather__wind_direction_west
            in 282..303 -> R.string.weather__wind_direction_wnw
            in 304..326 -> R.string.weather__wind_direction_nw
            in 327..348 -> R.string.weather__wind_direction_nnw
            else -> null
        }
        return resId?.let(res::getString).orEmpty()
    }
}