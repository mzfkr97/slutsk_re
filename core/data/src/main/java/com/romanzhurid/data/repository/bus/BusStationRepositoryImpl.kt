package com.romanzhurid.data.repository.bus

import com.romanzhurid.data.local.dao.BusStationDao
import com.romanzhurid.data.local.mapper.BusStationLocalMapper
import com.romanzhurid.domain.bus.model.BusStation
import com.romanzhurid.domain.bus.repository.BusStationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BusStationRepositoryImpl(
    private val busStationDao: BusStationDao,
    private val mapper: BusStationLocalMapper
) : BusStationRepository {

    override fun observeAllStations(): Flow<List<BusStation>> {
        return busStationDao.getAllStations().map { entities ->
            entities.map(mapper::map)
        }
    }

    override fun observeStationsByBusNumber(busNumber: Int): Flow<List<BusStation>> {
        return busStationDao.getStationsByBusNumber(busNumber).map { entities ->
            entities.map(mapper::map)
        }
    }

    override suspend fun getStationById(stationId: String): BusStation? {
        return busStationDao.getStationById(stationId)?.let(mapper::map)
    }
}
