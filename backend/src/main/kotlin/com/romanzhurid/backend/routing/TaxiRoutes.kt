package com.romanzhurid.backend.routing

import com.romanzhurid.backend.ext.ERROR
import com.romanzhurid.backend.ext.Routes
import com.romanzhurid.backend.models.TaxiCatalogItem
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json

private val taxiCatalogJson = Json {
    ignoreUnknownKeys = true
}

private object TaxiCatalogRepository {
    private const val TAXI_CATALOG_RESOURCE = "catalog__taxi.json"

    val items: List<TaxiCatalogItem> by lazy {
        val inputStream = TaxiCatalogRepository::class.java.classLoader
            .getResourceAsStream(TAXI_CATALOG_RESOURCE)
            ?: throw IllegalStateException("Resource $TAXI_CATALOG_RESOURCE not found")

        inputStream.use { stream ->
            taxiCatalogJson.decodeFromString<List<TaxiCatalogItem>>(stream.bufferedReader().readText())
        }
    }
}

fun Route.taxiRoutes() {
    route(Routes.TAXI) {
        get {
            try {
                call.respond(TaxiCatalogRepository.items)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf(ERROR to e.message))
            }
        }
    }
}
