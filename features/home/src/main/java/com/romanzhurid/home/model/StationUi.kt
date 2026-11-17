package com.romanzhurid.home.model

data class StationUi(
    val id: Int,
    val busNumber: Int,
    val startStation: String?,
    val endStation: String?,
    val routeName: String,
    val isFavorite: Boolean
)
