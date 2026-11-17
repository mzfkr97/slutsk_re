package com.romanzhurid.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bus_time",
    foreignKeys = [
        ForeignKey(
            entity = BusScheduleEntity::class,
            parentColumns = ["_id"],
            childColumns = ["bus_schedule_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BusTimeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int = 0,

    @ColumnInfo(name = "bus_schedule_id")
    val busScheduleId: Int,

    @ColumnInfo(name = "bus_number")
    val busNumber: String?,

    @ColumnInfo(name = "day_type")
    val dayType: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "note")
    val note: String? = null,
)
