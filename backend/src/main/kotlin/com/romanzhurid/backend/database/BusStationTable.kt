package com.romanzhurid.backend.database

import org.jetbrains.exposed.sql.Table

object BusStationTable : Table("bus_station") {
    val id = integer("id").autoIncrement()
    val busNumber = text("busNumber")
    val startStation = text("startStation")
    val workDay = text("workDay")
    val workDayTime = text("workDayTime")
    val holidayDay = text("holidayDay")
    val holidayDayTime = text("holidayDayTime")
    val destination = text("destination")
    val positionSort = integer("positionSort")
    val keyId = text("keyId")
    val stationId = text("stationId")
    val isStationAvailable = integer("isStationAvailable")

    override val primaryKey = PrimaryKey(id)
}
