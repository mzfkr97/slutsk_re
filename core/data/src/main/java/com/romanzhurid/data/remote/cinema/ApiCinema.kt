package com.romanzhurid.data.remote.cinema

import com.romanzhurid.data.remote.cinema.model.CinemaRemote
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiCinema {

    /** Техническая поддержка MegamagBy slutsk-app@yandex.ru
     параметры тутака: https://api.megamag.by/
     // https://api.megamag.by/v2/seances?
     // v2/seances?
     // time_start=2019-06-19T00%3A00%3A00%2B03%3A00
     // &time_end=2019-06-26T23%3A59%3A59%2B03%3A00&expand=show
     // &X-Megamag-Access-Token= bO5qn2poZnN12K6PO1GIjFukaCTau5nP
     https://api.megamag.by/v2/seances?time_start=2019-10-15T00%3A00%3A00%2B03%3A00&time_end=2019-10-16T23%3A59%3A59%2B03%3A00&expand=show&%26building_id=324505
     */

    @GET("/v2/seances")
    suspend fun getCinema(
        @Query("time_start") timeStart: String,
        @Query("time_end") timeEnd: String,
        @Query("expand") expand: String = "show",
        @Query("building_id") buildingId: Int = 324505
    ): List<CinemaRemote>

}
