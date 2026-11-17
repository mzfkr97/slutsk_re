package com.romanzhurid.domain.bus.model

data class BusSchedule(
    val id: Int,
    val busNumber: Int,
    val startStation: String?,
    val endStation: String?,
    val workingDays: String?,
    val workTime: String?,
    val weekend: String?,
    val weekendTime: String?,
    val allStation: String?,
    val keyId: String?,
)
