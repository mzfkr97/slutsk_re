package com.romanzhurid.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class TaxiPhone(
    val operator: String,
    val imageType: Int,
    val number: String
)

@Serializable
data class TaxiCatalogItem(
    val name: String,
    val phones: List<TaxiPhone>,
    val description: String,
    val url: String? = null
)
