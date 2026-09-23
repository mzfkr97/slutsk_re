package com.romanzhurid.domain.bus.repository

import com.romanzhurid.domain.bus.model.BusStation
import kotlinx.coroutines.flow.Flow

interface BusStationRepository {
    fun observeAllStations(): Flow<List<BusStation>>
    fun observeStationsByBusNumber(busNumber: Int): Flow<List<BusStation>>
    suspend fun getStationById(stationId: String): BusStation?
}
