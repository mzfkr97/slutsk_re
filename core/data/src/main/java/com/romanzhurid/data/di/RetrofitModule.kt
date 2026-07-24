package com.romanzhurid.data.di

import android.util.Log
import com.romanzhurid.data.BuildConfig
import com.romanzhurid.data.di.qualifier.BackendApi
import com.romanzhurid.data.di.qualifier.CinemaApi
import com.romanzhurid.data.remote.BusApi
import com.romanzhurid.data.remote.cinema.ApiCinema
import com.romanzhurid.data.remote.cinema.CinemaAuthInterceptor
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
import com.romanzhurid.data.di.qualifier.CurrencyApi as CurrencyQualifier

@Module(includes = [InterceptorsModule::class])
class RetrofitModule {

    companion object {
        private const val TIMEOUT_SECONDS = 30L
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

    private fun OkHttpClient.Builder.applyTimeouts(): OkHttpClient.Builder {
        return this
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
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
        okHttpClient: OkHttpClient,
        cinemaAuthInterceptor: CinemaAuthInterceptor,
    ): OkHttpClient {
        return okHttpClient
            .newBuilder()
            .addInterceptor(cinemaAuthInterceptor)
            .build()
    }

    @CinemaApi
    @Provides
    @Singleton
    fun provideCinemaRetrofit(
        @CinemaApi okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        return createRetrofit(
            okHttpClient = okHttpClient,
            json = json,
            baseUrl = BuildConfig.CINEMA_URL,
        )
    }

    @BackendApi
    @Provides
    @Singleton
    fun provideBackendRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        return createRetrofit(okHttpClient, json, BuildConfig.BASE_URL)
    }

    @CurrencyQualifier
    @Provides
    @Singleton
    fun provideCurrencyRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        return createRetrofit(okHttpClient, json, BuildConfig.CURRENCY_URL)
    }

    @CinemaApi
    @Provides
    @Singleton
    fun provideCinemaApi(
        @CinemaApi retrofit: Retrofit,
    ): ApiCinema {
        return retrofit.create(ApiCinema::class.java)
    }

    @BackendApi
    @Provides
    @Singleton
    fun provideBusApi(
        @BackendApi retrofit: Retrofit,
    ): BusApi {
        return retrofit.create(BusApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(
        @CurrencyQualifier retrofit: Retrofit,
    ): CurrencyApi {
        return retrofit.create(CurrencyApi::class.java)
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
}
