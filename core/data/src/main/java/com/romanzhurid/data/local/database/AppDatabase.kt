package com.romanzhurid.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.romanzhurid.data.local.dao.BusEndPointDao
import com.romanzhurid.data.local.dao.BusScheduleDao
import com.romanzhurid.data.local.dao.BusStationDao
import com.romanzhurid.data.local.dao.CurrencyDao
import com.romanzhurid.data.local.entity.BusEndPointEntity
import com.romanzhurid.data.local.entity.BusScheduleEntity
import com.romanzhurid.data.local.entity.BusStationEntity
import com.romanzhurid.data.local.entity.CurrencyEntity

@Database(
    entities = [
        CurrencyEntity::class,
        BusStationEntity::class,
        BusEndPointEntity::class,
        BusScheduleEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun busStationDao(): BusStationDao
    abstract fun busEndPointDao(): BusEndPointDao
    abstract fun busScheduleDao(): BusScheduleDao

    companion object {
        private const val PREPACKAGED_DB_NAME = "slutsk_app_database.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = PREPACKAGED_DB_NAME
                )
                    .createFromAsset(PREPACKAGED_DB_NAME)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also {
                        instance = it
                    }
            }
        }
    }
}
