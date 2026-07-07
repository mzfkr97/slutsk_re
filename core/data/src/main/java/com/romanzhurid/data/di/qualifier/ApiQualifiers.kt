package com.romanzhurid.data.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackendApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YandexApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CinemaApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrencyApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BelarusbankApi
