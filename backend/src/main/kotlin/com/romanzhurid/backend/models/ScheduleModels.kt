package com.romanzhurid.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleItem(
    val id: Int,
    val busNumber: String,
    val startStation: String,
    val destination: String,
    val workDayTime: String,
    val holidayDayTime: String? = null,
    val positionSort: Int? = null,
    val keyId: String? = null
)

@Serializable
data class BusDetail(
    val id: Int,
    val busNumber: String,
    val startStation: String,
    val destination: String,
    val workDay: String,
    val workDayTime: String,
    val holidayDay: String,
    val holidayDayTime: String? = null,
    val positionSort: Int? = null,
    val keyId: String? = null,
    val stationId: String? = null,
    val isStationAvailable: Int? = null
)

@Serializable
data class StationSchedule(
    val id: Int,
    val busNumber: String,
    val destination: String,
    val workDayTime: String,
    val holidayDayTime: String? = null,
    val positionSort: Int? = null,
    val stationId: String? = null,
    val isStationAvailable: Int? = null
)
