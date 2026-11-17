package com.romanzhurid.domain.bus.interactor

import com.romanzhurid.domain.bus.model.BusSchedule
import com.romanzhurid.domain.bus.model.BusTime
import com.romanzhurid.domain.bus.repository.BusScheduleRepository
import kotlinx.coroutines.flow.Flow

class BusScheduleInteractor(
    private val repository: BusScheduleRepository
) {
    fun observeAllSchedules(): Flow<List<BusSchedule>> {
        return repository.observeAllSchedules()
    }

    fun observeSchedulesByBusNumber(busNumber: Int): Flow<List<BusSchedule>> {
        return repository.observeSchedulesByBusNumber(busNumber)
    }

    fun observeTimesByBusNumber(busNumber: Int): Flow<List<BusTime>> {
        return repository.observeTimesByBusNumber(busNumber)
    }
}
