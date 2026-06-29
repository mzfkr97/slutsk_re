package com.romanzhurid.backend

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import com.romanzhurid.backend.routing.configureRouting
import com.romanzhurid.backend.database.DatabaseFactory
import com.romanzhurid.backend.ext.printEndPoints

fun main() {
    DatabaseFactory.init()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = false
            isLenient = true
        })
    }
    configureRouting()
    printEndPoints()
}
