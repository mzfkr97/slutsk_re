package com.romanzhurid.data.remote.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationScheduleRemote(
    @SerialName("id")
    val id: Int,

    @SerialName("busNumber")
    val busNumber: String,

    @SerialName("destination")
    val destination: String,

    @SerialName("workDayTime")
    val workDayTime: String,

    @SerialName("holidayDayTime")
    val holidayDayTime: String? = null,

    @SerialName("positionSort")
    val positionSort: Int? = null,

    @SerialName("stationId")
    val stationId: String? = null,

    @SerialName("isStationAvailable")
    val isStationAvailable: Int? = null
)