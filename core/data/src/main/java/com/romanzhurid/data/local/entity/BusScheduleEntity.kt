package com.romanzhurid.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bus_end_point")
data class BusScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int = 0,

    @ColumnInfo(name = "bus_number")
    val busNumber: String?,

    @ColumnInfo(name = "start_station")
    val startStation: String?,

    @ColumnInfo(name = "working_days")
    val workingDays: String?,

    @ColumnInfo(name = "work_time")
    val workTime: String?,

    @ColumnInfo(name = "weekend")
    val weekend: String?,

    @ColumnInfo(name = "weekend_time")
    val weekendTime: String?,

    @ColumnInfo(name = "all_station")
    val allStation: String?,

    @ColumnInfo(name = "key_id")
    val keyId: String?,

    @ColumnInfo(name = "end_station")
    val endStation: String?,
)
