package com.romanzhurid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romanzhurid.data.local.entity.BusTimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusTimeDao {

    @Query("""
        SELECT bus_time.* FROM bus_time
        INNER JOIN bus_end_point ON bus_time.bus_schedule_id = bus_end_point._id
        WHERE bus_end_point.bus_number = :busNumber
        ORDER BY bus_time.time ASC, bus_time._id ASC
    """)
    fun getTimesByRoute(busNumber: String): Flow<List<BusTimeEntity>>

    @Query("SELECT * FROM bus_time WHERE bus_schedule_id = :scheduleId ORDER BY time ASC")
    fun getTimesByScheduleId(scheduleId: Int): Flow<List<BusTimeEntity>>

    @Query("SELECT * FROM bus_time WHERE bus_schedule_id = :scheduleId AND day_type = :dayType ORDER BY time ASC")
    fun getTimesByScheduleAndDayType(scheduleId: Int, dayType: String): Flow<List<BusTimeEntity>>

    @Query("SELECT * FROM bus_time WHERE bus_number = :busNumber AND day_type = :dayType ORDER BY time ASC")
    fun getTimesByBusNumberAndDayType(busNumber: String, dayType: String): Flow<List<BusTimeEntity>>

    @Query("SELECT * FROM bus_time WHERE bus_schedule_id = :scheduleId AND day_type = :dayType AND time >= :currentTime ORDER BY time ASC")
    fun getUpcomingTimes(scheduleId: Int, dayType: String, currentTime: String): Flow<List<BusTimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BusTimeEntity>)

    @Query("DELETE FROM bus_time WHERE bus_schedule_id = :scheduleId")
    suspend fun deleteByScheduleId(scheduleId: Int)

    @Query("DELETE FROM bus_time")
    suspend fun deleteAll()
}
