package com.romanzhurid.bus.mapper

import com.romanzhurid.bus.model.BusScheduleUi
import com.romanzhurid.bus.model.BusTimeUi
import com.romanzhurid.domain.bus.model.BusDayType
import com.romanzhurid.domain.bus.model.BusSchedule
import com.romanzhurid.domain.bus.model.BusTime

class BusScheduleUiMapper {

    fun map(schedule: BusSchedule, times: List<BusTime>): BusScheduleUi {
        val scheduleTimes = times.filter { it.scheduleId == schedule.id }
        fun timesFor(vararg dayTypes: BusDayType): List<BusTimeUi> =
            scheduleTimes.filter { it.dayType in dayTypes }
                .sortedBy { it.time }
                .map { BusTimeUi(time = it.time, note = it.note) }

        return with(schedule) {
            BusScheduleUi(
                id = id,
                startStation = startStation.orEmpty(),
                endStation = endStation.orEmpty(),
                allDays = timesFor(BusDayType.ALL_DAYS),
                workTimes = timesFor(BusDayType.WORKDAY),
                weekendTimes = timesFor(BusDayType.WEEKEND),
                allStation = allStation.orEmpty(),
            )
        }
    }
}