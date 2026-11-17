package com.romanzhurid.domain.bus.interactor

import com.romanzhurid.domain.bus.model.BusEndPoint
import com.romanzhurid.domain.bus.repository.BusEndPointRepository
import kotlinx.coroutines.flow.Flow

class BusEndPointInteractor(
    private val repository: BusEndPointRepository
) {
    fun observeAll(): Flow<List<BusEndPoint>> {
        return repository.observeAll()
    }

    suspend fun getByBusNumber(busNumber: Int): BusEndPoint? {
        return repository.getByBusNumber(busNumber)
    }

    fun observeFavorites(): Flow<List<BusEndPoint>> {
        return repository.observeFavorites()
    }

    suspend fun toggleFavorite(id: Int, isFavorite: Boolean) {
        repository.toggleFavorite(id, isFavorite)
    }
}
