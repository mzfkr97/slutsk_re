package com.romanzhurid.data.repository.bus

import com.romanzhurid.data.local.dao.BusEndPointDao
import com.romanzhurid.data.local.mapper.BusEndPointLocalMapper
import com.romanzhurid.domain.bus.model.BusEndPoint
import com.romanzhurid.domain.bus.repository.BusEndPointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BusEndPointRepositoryImpl(
    private val busEndPointDao: BusEndPointDao,
    private val mapper: BusEndPointLocalMapper
) : BusEndPointRepository {

    override fun observeAll(): Flow<List<BusEndPoint>> {
        return busEndPointDao.getAll().map { entities ->
            entities.map(mapper::map)
        }
    }

    override suspend fun getByBusNumber(busNumber: Int): BusEndPoint? {
        return busEndPointDao.getByBusNumber(busNumber)?.let(mapper::map)
    }

    override fun observeFavorites(): Flow<List<BusEndPoint>> {
        return busEndPointDao.getFavorites().map { entities ->
            entities.map(mapper::map)
        }
    }

    override suspend fun toggleFavorite(id: Int, isFavorite: Boolean) {
        busEndPointDao.updateFavorite(id, isFavorite)
    }
}
