package com.romanzhurid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.romanzhurid.data.local.entity.BusStationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusStationDao {

    @Query("SELECT * FROM bus_station ORDER BY positionSort")
    fun getAllStations(): Flow<List<BusStationEntity>>

    @Query("SELECT * FROM bus_station WHERE busNumber = :busNumber ORDER BY positionSort")
    fun getStationsByBusNumber(busNumber: Int): Flow<List<BusStationEntity>>

    @Query("SELECT * FROM bus_station WHERE stationId = :stationId")
    suspend fun getStationById(stationId: String): BusStationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<BusStationEntity>)

    @Update
    suspend fun updateStation(station: BusStationEntity)

    @Query("SELECT * FROM bus_station WHERE isModified = 1")
    fun getModifiedStations(): Flow<List<BusStationEntity>>

    @Query("DELETE FROM bus_station")
    suspend fun deleteAllStations()

    @Query("UPDATE bus_station SET isModified = 1 WHERE id = :id")
    suspend fun markAsModified(id: Int)

}
