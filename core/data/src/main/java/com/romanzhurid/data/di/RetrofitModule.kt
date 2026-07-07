package com.romanzhurid.data.di

import com.romanzhurid.data.BuildConfig
import com.romanzhurid.data.di.qualifier.BackendApi
import com.romanzhurid.data.di.qualifier.BelarusbankApi
import com.romanzhurid.data.di.qualifier.CinemaApi
import com.romanzhurid.data.di.qualifier.CurrencyApi as CurrencyQualifier
import com.romanzhurid.data.di.qualifier.WeatherApi
import com.romanzhurid.data.di.qualifier.YandexApi
import com.romanzhurid.data.remote.BusApi
import com.romanzhurid.data.remote.currencies.CurrencyApi
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    companion object {
        private const val TIMEOUT_SECONDS = 30L
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun OkHttpClient.Builder.applyTimeouts(): OkHttpClient.Builder {
        return this
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .applyTimeouts()
            .build()
    }

    @YandexApi
    @Provides
    @Singleton
    fun provideYandexOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .applyTimeouts()
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("apikey", BuildConfig.YANDEX_MAP_API_KEY)
                    .build()
                chain.proceed(original.newBuilder().url(url).build())
            }
            .build()
    }

    @WeatherApi
    @Provides
    @Singleton
    fun provideWeatherOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .applyTimeouts()
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("appid", BuildConfig.KEY_WEATHER_API)
                    .build()
                chain.proceed(original.newBuilder().url(url).build())
            }
            .build()
    }

    @BackendApi
    @Provides
    @Singleton
    fun provideBackendOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .applyTimeouts()
            .build()
    }

    @CinemaApi
    @Provides
    @Singleton
    fun provideCinemaOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .applyTimeouts()
            .build()
    }

    @CurrencyQualifier
    @Provides
    @Singleton
    fun provideCurrencyOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .applyTimeouts()
            .build()
    }

    @BelarusbankApi
    @Provides
    @Singleton
    fun provideBelarusbankOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .applyTimeouts()
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }

    @BackendApi
    @Provides
    @Singleton
    fun provideBackendRetrofit(
        @BackendApi okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return createRetrofit(okHttpClient, json, BuildConfig.BASE_URL)
    }

    @YandexApi
    @Provides
    @Singleton
    fun provideYandexRetrofit(
        @YandexApi okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return createRetrofit(okHttpClient, json, BuildConfig.YANDEX_BASE_API)
    }

    @CinemaApi
    @Provides
    @Singleton
    fun provideCinemaRetrofit(
        @CinemaApi okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return createRetrofit(okHttpClient, json, BuildConfig.CINEMA_URL)
    }

    @WeatherApi
    @Provides
    @Singleton
    fun provideWeatherRetrofit(
        @WeatherApi okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return createRetrofit(okHttpClient, json, BuildConfig.BASE_URL_WHEATHER)
    }

    @CurrencyQualifier
    @Provides
    @Singleton
    fun provideCurrencyRetrofit(
        @CurrencyQualifier okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return createRetrofit(okHttpClient, json, BuildConfig.CURRENCY_URL)
    }

    @BelarusbankApi
    @Provides
    @Singleton
    fun provideBelarusbankRetrofit(
        @BelarusbankApi okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return createRetrofit(okHttpClient, json, BuildConfig.BELARUSBANK_URL)
    }

    @Provides
    @Singleton
    fun provideBusApi(@BackendApi retrofit: Retrofit): BusApi {
        return retrofit.create(BusApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(@CurrencyQualifier retrofit: Retrofit): CurrencyApi {
        return retrofit.create(CurrencyApi::class.java)
    }

    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
        baseUrl: String
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
