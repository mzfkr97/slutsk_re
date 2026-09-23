package com.romanzhurid.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bus_end_point_list")
data class BusEndPointEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int = 0,

    @ColumnInfo(name = "bus_number")
    val busNumber: Int,

    @ColumnInfo(name = "start_station")
    val startStation: String?,

    @ColumnInfo(name = "end_station")
    val endStation: String?,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
)