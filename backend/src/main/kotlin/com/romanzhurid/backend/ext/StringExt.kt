package com.romanzhurid.backend.ext

const val EMPTY_STRING = ""
const val SPACE = " "
const val ERROR = "error"

internal fun printEndPoints() {
    //  | jq cd /Users/rzhurid/AndroidStudioProjects/SlutskRe
    //  ./gradlew :backend:run
    //  curl http://localhost:8080/schedule/1
    //  curl http://localhost:8080/schedule | jq .
    //  curl http://localhost:8080/delivery | jq .

    // lsof -i :8080  kill 9723
    println("API эндпоинты:")
    println("  GET  /                    - Проверка статуса")
    println("  GET  /schedule            - Все расписания")
    println("  GET  /schedule/{id}       - Расписание по ID")
    println("  GET  /schedule/bus/{number} - Расписание по номеру автобуса")
    println("  GET  /schedule/station/{stationName} - Расписание по станции")
    println("  GET  /taxi                - Каталог такси")
    println("  GET  /delivery          - Доставка")
}
