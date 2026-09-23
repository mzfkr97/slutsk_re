package com.romanzhurid.domain.bus.interactor

import com.romanzhurid.domain.bus.model.BusStation
import com.romanzhurid.domain.bus.repository.BusStationRepository
import kotlinx.coroutines.flow.Flow

class BusStationInteractor(
    private val repository: BusStationRepository
) {
    fun observeAllStations(): Flow<List<BusStation>> {
        return repository.observeAllStations()
    }

    fun observeStationsByBusNumber(busNumber: Int): Flow<List<BusStation>> {
        return repository.observeStationsByBusNumber(busNumber)
    }

    suspend fun getStationById(stationId: String): BusStation? {
        return repository.getStationById(stationId)
    }
}
