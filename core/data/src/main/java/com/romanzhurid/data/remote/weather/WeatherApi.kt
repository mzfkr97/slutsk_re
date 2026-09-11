package com.romanzhurid.data.remote.weather

import com.romanzhurid.data.remote.model.OpenWeatherRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    /**
     * http://api.openweathermap.org/data/2.5/weather?id=621741&units=metric&lang=ru&appid=5dc3bd6f3df898801a81bd7803e54ccc
     * */
    @GET("weather")
    suspend fun getWeather(
        @Query("id") id: String,
        @Query("lang") lang: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): OpenWeatherRemote

    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "ru",
        @Query("appid") apiKey: String,
    ): OpenWeatherRemote
}
