package com.romanzhurid.bus.ui

import com.romanzhurid.bus.model.BusScheduleUi
import com.romanzhurid.domain.bus.model.BusDayType
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

internal data class NextDeparture(val dayType: BusDayType, val index: Int)

internal fun findNextDeparture(schedule: BusScheduleUi, now: LocalDateTime): NextDeparture? {
    val isWeekend = now.dayOfWeek == DayOfWeek.SATURDAY || now.dayOfWeek == DayOfWeek.SUNDAY
    val dayType = if (isWeekend) BusDayType.WEEKEND else BusDayType.WORKDAY
    val times = if (isWeekend) schedule.weekendTimes else schedule.workTimes
    val currentMinute = now.toLocalTime().truncatedTo(ChronoUnit.MINUTES)
    val next = sequenceOf(BusDayType.ALL_DAYS to schedule.allDays, dayType to times)
        .flatMap { (type, departures) ->
            departures.asSequence().mapIndexed { index, departure ->
                NextDeparture(type, index) to LocalTime.parse(departure.time)
            }
        }
        .filter { (_, time) -> !time.isBefore(currentMinute) }
        .minByOrNull { (_, time) -> time }
        ?: return null
    return next.first
}
