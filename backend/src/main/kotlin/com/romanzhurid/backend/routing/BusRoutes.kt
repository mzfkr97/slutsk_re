package com.romanzhurid.backend.routing

import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import com.romanzhurid.backend.database.BusScheduleDAO
import com.romanzhurid.backend.ext.ERROR
import com.romanzhurid.backend.ext.Routes

fun Route.busRoutes() {
    route(Routes.SCHEDULE) {
        get {
            try {
                val schedules = BusScheduleDAO.getAllSchedules()
                if (schedules.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, mapOf("message" to "No schedules found"))
                } else {
                    call.respond(schedules)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf(ERROR to e.message))
            }
        }

        get(Routes.SCHEDULE_BY_BUS_NUMBER) {
            try {
                val number = call.parameters["number"]
                if (number.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf(ERROR to "Invalid bus number"))
                    return@get
                }
                
                val schedules = BusScheduleDAO.getScheduleByBusNumber(number)
                if (schedules.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, mapOf("message" to "No schedules found for this bus"))
                } else {
                    call.respond(schedules)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf(ERROR to e.message))
            }
        }

        get(Routes.SCHEDULE_BY_STATION_NAME) {
            try {
                val stationName = call.parameters["stationName"]
                if (stationName.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf(ERROR to "Station name required"))
                    return@get
                }
                
                val schedules = BusScheduleDAO.getScheduleByStation(stationName)
                if (schedules.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, mapOf("message" to "No schedules found for this station"))
                } else {
                    call.respond(schedules)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf(ERROR to e.message))
            }
        }
        
        get(Routes.SCHEDULE_BY_ID) {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf(ERROR to "Invalid ID"))
                    return@get
                }
                
                val schedule = BusScheduleDAO.getScheduleById(id)
                if (schedule == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("message" to "Schedule not found"))
                } else {
                    call.respond(schedule)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf(ERROR to e.message))
            }
        }
    }
}
