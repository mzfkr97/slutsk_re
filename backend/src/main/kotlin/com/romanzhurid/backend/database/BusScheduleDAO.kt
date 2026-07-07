package com.romanzhurid.backend.database

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import com.romanzhurid.backend.models.ScheduleItem
import com.romanzhurid.backend.models.BusDetail
import com.romanzhurid.backend.models.StationSchedule

object BusScheduleDAO {
    fun getAllSchedules(): List<ScheduleItem> {
        return transaction {
            BusStationTable.selectAll()
                .map { rowToScheduleItem(it) }
        }
    }

    fun getScheduleById(id: Int): BusDetail? {
        return transaction {
            BusStationTable
                .selectAll().where { BusStationTable.id eq id }
                .map { rowToBusDetail(it) }
                .firstOrNull()
        }
    }

    fun getScheduleByBusNumber(busNumber: String): List<ScheduleItem> {
        return transaction {
            BusStationTable
                .selectAll().where { BusStationTable.busNumber like "%$busNumber%" }
                .map { rowToScheduleItem(it) }
        }
    }

    fun getScheduleByStation(station: String): List<StationSchedule> {
        return transaction {
            BusStationTable
                .selectAll().where { BusStationTable.startStation like "%$station%" }
                .map { rowToStationSchedule(it) }
        }
    }

    private fun rowToScheduleItem(row: ResultRow): ScheduleItem {
        return ScheduleItem(
            id = row[BusStationTable.id],
            busNumber = row[BusStationTable.busNumber],
            startStation = row[BusStationTable.startStation] ?: "",
            destination = row[BusStationTable.destination] ?: "",
            workDayTime = row[BusStationTable.workDayTime] ?: "",
            holidayDayTime = row[BusStationTable.holidayDayTime],
            positionSort = row[BusStationTable.positionSort],
            keyId = row[BusStationTable.keyId]
        )
    }

    private fun rowToBusDetail(row: ResultRow): BusDetail {
        return BusDetail(
            id = row[BusStationTable.id],
            busNumber = row[BusStationTable.busNumber],
            startStation = row[BusStationTable.startStation] ?: "",
            destination = row[BusStationTable.destination] ?: "",
            workDay = row[BusStationTable.workDay] ?: "",
            workDayTime = row[BusStationTable.workDayTime] ?: "",
            holidayDay = row[BusStationTable.holidayDay] ?: "",
            holidayDayTime = row[BusStationTable.holidayDayTime] ?: "",
            positionSort = row[BusStationTable.positionSort],
            keyId = row[BusStationTable.keyId],
            stationId = row[BusStationTable.stationId],
            isStationAvailable = row[BusStationTable.isStationAvailable]
        )
    }

    private fun rowToStationSchedule(row: ResultRow): StationSchedule {
        return StationSchedule(
            id = row[BusStationTable.id],
            busNumber = row[BusStationTable.busNumber],
            destination = row[BusStationTable.destination] ?: "",
            workDayTime = row[BusStationTable.workDayTime] ?: "",
            holidayDayTime = row[BusStationTable.holidayDayTime],
            positionSort = row[BusStationTable.positionSort],
            stationId = row[BusStationTable.stationId],
            isStationAvailable = row[BusStationTable.isStationAvailable]
        )
    }
}
