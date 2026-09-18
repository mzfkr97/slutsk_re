package com.romanzhurid.data.local.mapper

import com.romanzhurid.data.local.entity.BusStationEntity
import com.romanzhurid.domain.bus.model.BusStation

class BusStationLocalMapper {
    fun map(entity: BusStationEntity): BusStation {
        return with(entity) {
            BusStation(
                id = id,
                busNumber = busNumber,
                startStation = startStation,
                destination = destination,
                workDay = workDay,
                workDayTime = workDayTime,
                holidayDay = holidayDay,
                holidayDayTime = holidayDayTime,
                positionSort = positionSort,
                keyId = keyId,
                stationId = stationId,
                isStationAvailable = isStationAvailable != 0
            )
        }
    }
}
