package com.romanzhurid.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class BusSchedule(
    val id: Int, 
    val routeNumber: String, 
    val departureTime: String
)
