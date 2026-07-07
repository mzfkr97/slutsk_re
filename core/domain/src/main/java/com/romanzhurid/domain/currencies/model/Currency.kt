package com.romanzhurid.domain.currencies.model

data class Currency(
    val id: Int,
    val abbreviation: String,
    val name: String,
    val officialRate: Double,
    val scale: Int,
    val date: String,
    val isFavorite: Boolean
)
