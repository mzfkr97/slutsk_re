package com.romanzhurid.domain.bus.repository

import com.romanzhurid.domain.bus.model.BusEndPoint
import kotlinx.coroutines.flow.Flow

interface BusEndPointRepository {
    fun observeAll(): Flow<List<BusEndPoint>>
    suspend fun getByBusNumber(busNumber: Int): BusEndPoint?
    fun observeFavorites(): Flow<List<BusEndPoint>>
    suspend fun toggleFavorite(id: Int, isFavorite: Boolean)
}
