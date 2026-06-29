package com.romanzhurid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romanzhurid.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies ORDER BY isFavorite DESC, abbreviation")
    fun getAllCurrencies(): Flow<List<CurrencyEntity>>

    @Query("SELECT id FROM currencies WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Int>

    @Query("UPDATE currencies SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Int)

    @Query("SELECT * FROM currencies WHERE id = :id")
    suspend fun getCurrencyById(id: Int): CurrencyEntity?

    @Query("SELECT MAX(updatedAt) FROM currencies")
    fun getLastUpdateTime(): Flow<Long?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)

    @Query("DELETE FROM currencies")
    suspend fun deleteAllCurrencies()
}
