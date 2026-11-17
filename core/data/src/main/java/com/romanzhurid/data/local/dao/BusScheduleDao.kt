package com.romanzhurid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romanzhurid.data.local.entity.BusScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusScheduleDao {

    @Query("SELECT * FROM bus_end_point ORDER BY bus_number")
    fun getAll(): Flow<List<BusScheduleEntity>>

    @Query("SELECT * FROM bus_end_point WHERE bus_number = :busNumber")
    fun getByBusNumber(busNumber: String): Flow<List<BusScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BusScheduleEntity>)

    @Query("DELETE FROM bus_end_point")
    suspend fun deleteAll()
}
