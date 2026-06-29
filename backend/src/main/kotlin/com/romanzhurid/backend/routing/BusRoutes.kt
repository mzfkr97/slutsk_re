package com.romanzhurid.backend.routing

import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.romanzhurid.backend.models.BusSchedule

fun Route.busRoutes() {
    route("/schedule") {
        get {
            // Mock data for now
            val sampleData = listOf(
                BusSchedule(1, "201", "08:00"),
                BusSchedule(2, "202", "09:30"),
                BusSchedule(3, "203", "11:15")
            )
            call.respond(sampleData)
        }
        
        get("/{id}") {
            val id = call.parameters["id"]
            call.respondText("Getting schedule for route ID: $id")
        }
    }
}
