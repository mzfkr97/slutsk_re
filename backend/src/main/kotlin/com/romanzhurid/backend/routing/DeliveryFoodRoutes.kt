package com.romanzhurid.backend.routing

import com.romanzhurid.backend.ext.ERROR
import com.romanzhurid.backend.ext.Routes
import com.romanzhurid.backend.models.DeliveryCatalogItem
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

private val deliveryCatalogJson = Json {
    ignoreUnknownKeys = true
}

private object DeliveryFoodRepository {
    private const val CATALOG_RESOURCE = "delivery_food.json"

    val items: List<DeliveryCatalogItem> by lazy {
        val inputStream = DeliveryFoodRepository::class.java.classLoader
            .getResourceAsStream(CATALOG_RESOURCE)
            ?: throw IllegalStateException("Resource $CATALOG_RESOURCE not found")

        inputStream.use { stream ->
            deliveryCatalogJson.decodeFromString<List<DeliveryCatalogItem>>(stream.bufferedReader().readText())
        }
    }
}

fun Route.deliveryFoodRoutes() {
    route(Routes.DELIVERY_FOOD) {
        get {
            try {
                call.respond(DeliveryFoodRepository.items)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf(ERROR to e.message))
            }
        }
    }
}
