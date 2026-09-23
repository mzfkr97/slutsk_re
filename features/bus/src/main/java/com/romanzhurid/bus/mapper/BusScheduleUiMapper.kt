package com.romanzhurid.bus.mapper

import com.romanzhurid.bus.model.BusScheduleUi
import com.romanzhurid.domain.bus.model.BusSchedule

class BusScheduleUiMapper {

    fun map(schedule: BusSchedule): BusScheduleUi {
        return with(schedule) {
            BusScheduleUi(
                id = id,
                startStation = startStation.orEmpty(),
                endStation = endStation.orEmpty(),
                workingDays = workingDays.orEmpty(),
                workTime = workTime.orEmpty(),
                weekend = weekend.orEmpty(),
                weekendTime = weekendTime.orEmpty(),
                allStation = allStation.orEmpty(),
            )
        }
    }
}
