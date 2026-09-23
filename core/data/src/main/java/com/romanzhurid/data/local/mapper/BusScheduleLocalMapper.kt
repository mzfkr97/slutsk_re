package com.romanzhurid.data.local.mapper

import com.romanzhurid.data.local.entity.BusScheduleEntity
import com.romanzhurid.domain.bus.model.BusSchedule

class BusScheduleLocalMapper {
    fun map(entity: BusScheduleEntity): BusSchedule {
        return with(entity) {
            BusSchedule(
                id = id,
                busNumber = busNumber?.toIntOrNull() ?: 0,
                startStation = startStation,
                endStation = endStation,
                workingDays = workingDays,
                workTime = workTime,
                weekend = weekend,
                weekendTime = weekendTime,
                allStation = allStation,
                keyId = keyId,
            )
        }
    }
}
