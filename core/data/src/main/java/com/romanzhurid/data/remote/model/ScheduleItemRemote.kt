package com.romanzhurid.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleItemRemote(
    @SerialName("id")
    val id: Int,

    @SerialName("busNumber")
    val busNumber: String,

    @SerialName("startStation")
    val startStation: String,

    @SerialName("destination")
    val destination: String,

    @SerialName("workDayTime")
    val workDayTime: String,

    @SerialName("holidayDayTime")
    val holidayDayTime: String? = null,

    @SerialName("positionSort")
    val positionSort: Int? = null,

    @SerialName("keyId")
    val keyId: String? = null
)
