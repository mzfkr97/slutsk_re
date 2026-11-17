package com.romanzhurid.bus.model

import androidx.compose.runtime.Immutable

@Immutable
data class BusScheduleUi(
    val id: Int,
    val startStation: String,
    val endStation: String,
    val allDays: List<BusTimeUi>,
    val workTimes: List<BusTimeUi>,
    val weekendTimes: List<BusTimeUi>,
    val allStation: String,
)

@Immutable
data class BusTimeUi(
    val time: String,
    val note: String?,
)
