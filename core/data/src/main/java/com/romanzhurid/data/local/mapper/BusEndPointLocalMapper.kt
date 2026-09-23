package com.romanzhurid.data.local.mapper

import com.romanzhurid.data.local.entity.BusEndPointEntity
import com.romanzhurid.domain.bus.model.BusEndPoint

class BusEndPointLocalMapper {
    fun map(entity: BusEndPointEntity): BusEndPoint {
        return with(entity) {
            BusEndPoint(
                id = id,
                busNumber = busNumber,
                startStation = startStation,
                endStation = endStation,
                isFavorite = isFavorite
            )
        }
    }
}
