package com.romanzhurid.backend.ext

const val EMPTY_STRING = ""
const val SPACE = " "
const val ERROR = "error"

internal fun printEndPoints() {
    //  | jq cd /Users/rzhurid/AndroidStudioProjects/SlutskRe
    //  ./gradlew :backend:run
    println("API эндпоинты:")
    println("  GET  /                    - Проверка статуса")
    println("  GET  /schedule            - Все расписания")
    println("  GET  /schedule/{id}       - Расписание по ID")
    println("  GET  /schedule/bus/{number} - Расписание по номеру автобуса")
    println("  GET  /schedule/station/{stationName} - Расписание по станции")
}
