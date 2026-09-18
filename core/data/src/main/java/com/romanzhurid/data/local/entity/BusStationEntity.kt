package com.romanzhurid.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bus_station")
data class BusStationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val busNumber: Int,
    val startStation: String,
    val workDay: String,
    val workDayTime: String,
    val holidayDay: String,
    val holidayDayTime: String?,
    val destination: String,
    val positionSort: Int,
    val keyId: String,
    val stationId: String,
    @ColumnInfo(name = "isStationAvailable")
    val isStationAvailable: Int,

    @ColumnInfo(defaultValue = "0")
    val isModified: Boolean = false,
)
