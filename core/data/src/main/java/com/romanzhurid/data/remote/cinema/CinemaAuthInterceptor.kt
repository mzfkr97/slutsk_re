package com.romanzhurid.data.remote.cinema

import com.romanzhurid.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class CinemaAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                "X-Megamag-Access-Token",
                BuildConfig.CINEMA_API_KEY
            )
            .build()
        return chain.proceed(request)
    }
}
