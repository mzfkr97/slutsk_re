package com.romanzhurid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import com.romanzhurid.data.local.entity.BusEndPointEntity

@Dao
interface BusEndPointDao {

    @Query("SELECT * FROM bus_end_point_list ORDER BY bus_number")
    fun getAll(): Flow<List<BusEndPointEntity>>

    @Query("SELECT * FROM bus_end_point_list WHERE bus_number = :busNumberLIMIT 1")
    suspend fun getByBusNumber(busNumber: Int): BusEndPointEntity?

    @Query(" SELECT * FROM bus_end_point_list WHERE is_favorite = 1 ORDER BY bus_number")
    fun getFavorites(): Flow<List<BusEndPointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BusEndPointEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BusEndPointEntity>)

    @Update
    suspend fun update(item: BusEndPointEntity)

    @Delete
    suspend fun delete(item: BusEndPointEntity)

    @Query("DELETE FROM bus_end_point_list")
    suspend fun deleteAll()

    @Query("UPDATE bus_end_point_list SET is_favorite = :isFavorite WHERE _id = :id")
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )
}