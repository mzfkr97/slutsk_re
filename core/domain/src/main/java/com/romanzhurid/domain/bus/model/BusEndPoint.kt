package com.romanzhurid.domain.bus.model

data class BusEndPoint(
    val id: Int,
    val busNumber: Int,
    val startStation: String?,
    val endStation: String?,
    val isFavorite: Boolean
)
