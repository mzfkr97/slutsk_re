package com.romanzhurid.domain.bus.repository

import com.romanzhurid.domain.bus.model.BusSchedule
import com.romanzhurid.domain.bus.model.BusTime
import kotlinx.coroutines.flow.Flow

interface BusScheduleRepository {
    fun observeAllSchedules(): Flow<List<BusSchedule>>
    fun observeSchedulesByBusNumber(busNumber: Int): Flow<List<BusSchedule>>
    fun observeTimesByBusNumber(busNumber: Int): Flow<List<BusTime>>
}
