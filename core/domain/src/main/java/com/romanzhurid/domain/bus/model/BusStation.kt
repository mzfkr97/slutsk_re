package com.romanzhurid.domain.bus.model

data class BusStation(
    val id: Int,
    val busNumber: Int,
    val startStation: String,
    val destination: String,
    val workDay: String,
    val workDayTime: String,
    val holidayDay: String,
    val holidayDayTime: String?,
    val positionSort: Int,
    val keyId: String,
    val stationId: String,
    val isStationAvailable: Boolean
)
