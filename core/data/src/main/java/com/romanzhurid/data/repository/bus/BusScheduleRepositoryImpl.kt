package com.romanzhurid.data.repository.bus

import com.romanzhurid.data.local.dao.BusScheduleDao
import com.romanzhurid.data.local.dao.BusTimeDao
import com.romanzhurid.data.local.mapper.BusScheduleLocalMapper
import com.romanzhurid.domain.bus.model.BusDayType
import com.romanzhurid.domain.bus.model.BusSchedule
import com.romanzhurid.domain.bus.model.BusTime
import com.romanzhurid.domain.bus.repository.BusScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BusScheduleRepositoryImpl(
    private val busScheduleDao: BusScheduleDao,
    private val busTimeDao: BusTimeDao,
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

    override fun observeTimesByBusNumber(busNumber: Int): Flow<List<BusTime>> {
        return busTimeDao.getTimesByRoute(busNumber.toString()).map { entities ->
            entities.map { entity ->
                BusTime(
                    scheduleId = entity.busScheduleId,
                    dayType = BusDayType.valueOf(entity.dayType),
                    time = entity.time,
                    note = entity.note,
                )
            }
        }
    }
}
