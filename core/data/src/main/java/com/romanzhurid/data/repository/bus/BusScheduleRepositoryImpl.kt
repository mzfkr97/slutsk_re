package com.romanzhurid.data.repository.bus

import com.romanzhurid.data.local.dao.BusScheduleDao
import com.romanzhurid.data.local.mapper.BusScheduleLocalMapper
import com.romanzhurid.domain.bus.model.BusSchedule
import com.romanzhurid.domain.bus.repository.BusScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BusScheduleRepositoryImpl(
    private val busScheduleDao: BusScheduleDao,
    private val mapper: BusScheduleLocalMapper
) : BusScheduleRepository {

    override fun observeAllSchedules(): Flow<List<BusSchedule>> {
        return busScheduleDao.getAll().map { entities ->
            entities.map(mapper::map)
        }
    }

    override fun observeSchedulesByBusNumber(busNumber: Int): Flow<List<BusSchedule>> {
        return busScheduleDao.getByBusNumber(busNumber.toString()).map { entities ->
            entities.map(mapper::map)
        }
    }
}
