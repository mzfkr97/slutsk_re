package com.romanzhurid.bus.model

import androidx.compose.runtime.Immutable

@Immutable
data class BusScheduleUi(
    val id: Int,
    val startStation: String,
    val endStation: String,
    val workingDays: String,
    val workTime: String,
    val weekend: String,
    val weekendTime: String,
    val allStation: String,
)
