package com.romanzhurid.data.di

import com.romanzhurid.data.BuildConfig
import com.romanzhurid.data.remote.BusApi
import com.romanzhurid.data.remote.cinema.CinemaApi
import com.romanzhurid.data.remote.cinema.CinemaAuthInterceptor
import com.romanzhurid.data.remote.currencies.CurrencyApi
import com.romanzhurid.data.remote.weather.WeatherApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {
    includes(interceptorsModule)

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }

    single {
        val loggingInterceptor: HttpLoggingInterceptor = get()
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .build()
    }

    single(named("cinemaHttpClient")) {
        val okHttpClient: OkHttpClient = get()
        val cinemaAuthInterceptor: CinemaAuthInterceptor = get()
        okHttpClient
            .newBuilder()
            .addInterceptor(cinemaAuthInterceptor)
            .build()
    }

    single(named("cinemaRetrofit")) {
        val okHttpClient: OkHttpClient = get(named("cinemaHttpClient"))
        val json: Json = get()
        createRetrofit(okHttpClient, json, BuildConfig.CINEMA_URL)
    }

    single(named("backendRetrofit")) {
        val okHttpClient: OkHttpClient = get()
        val json: Json = get()
        createRetrofit(okHttpClient, json, BuildConfig.BASE_URL)
    }

    single(named("currencyRetrofit")) {
        val okHttpClient: OkHttpClient = get()
        val json: Json = get()
        createRetrofit(okHttpClient, json, BuildConfig.CURRENCY_URL)
    }

    single(named("weatherRetrofit")) {
        val okHttpClient: OkHttpClient = get()
        val json: Json = get()
        createRetrofit(okHttpClient, json, BuildConfig.BASE_URL_WHEATHER)
    }

    single<CinemaApi> {
        val retrofit: Retrofit = get(named("cinemaRetrofit"))
        retrofit.create(CinemaApi::class.java)
    }

    single<BusApi> {
        val retrofit: Retrofit = get(named("backendRetrofit"))
        retrofit.create(BusApi::class.java)
    }

    single<CurrencyApi> {
        val retrofit: Retrofit = get(named("currencyRetrofit"))
        retrofit.create(CurrencyApi::class.java)
    }

    single<WeatherApi> {
        get<Retrofit>(named("weatherRetrofit")).create(WeatherApi::class.java)
    }
}

private fun createRetrofit(
    okHttpClient: OkHttpClient,
    json: Json,
    baseUrl: String,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()
}
