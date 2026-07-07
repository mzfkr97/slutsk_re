package com.romanzhurid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val abbreviation: String,
    val scale: Int,
    val officialRate: Double,
    val date: String,
    val updatedAt: Long,
    val isFavorite: Boolean
)
