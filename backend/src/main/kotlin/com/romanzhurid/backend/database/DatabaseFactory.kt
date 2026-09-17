package com.romanzhurid.backend.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcUrl = "jdbc:sqlite:app_database.db"
        
        val dbFile = File("app_database.db")
        if (!dbFile.exists()) {
            println("WARNING: Database file not found at ${dbFile.absolutePath}")
        } else {
            println("Database file found at ${dbFile.absolutePath}")
        }

        Database.connect(jdbcUrl, driverClassName)
        
        transaction {
            SchemaUtils.createMissingTablesAndColumns(BusStationTable)
        }
    }

    // Вспомогательная функция для выполнения запросов
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        transaction {
            // Здесь можно добавить логику для асинхронного выполнения, 
            // если база будет нагруженной
            kotlinx.coroutines.runBlocking { block() }
        }
}
