package com.romanzhurid.data.remote

import com.romanzhurid.data.remote.model.BusDetailRemote
import com.romanzhurid.data.remote.model.ScheduleItemRemote
import com.romanzhurid.data.remote.model.StationScheduleRemote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BusApi {

    @GET("schedule")
    suspend fun getAllSchedules(): List<ScheduleItemRemote>

    @GET("schedule/bus/{number}")
    suspend fun getScheduleByBusNumber(
        @Path("number") number: String
    ): List<BusDetailRemote>

    @GET("schedule/station")
    suspend fun getScheduleByStationName(
        @Query("stationName") stationName: String
    ): List<StationScheduleRemote>

    @GET("schedule/{id}")
    suspend fun getScheduleById(
        @Path("id") id: Int
    ): ScheduleItemRemote
}
