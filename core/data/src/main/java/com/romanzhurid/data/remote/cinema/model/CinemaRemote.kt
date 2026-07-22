package com.romanzhurid.data.remote.cinema.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CinemaRemote(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("building_id")
    val buildingId: Int? = null,

    @SerialName("hall_id")
    val hallId: Int? = null,

    @SerialName("show_id")
    val showId: Int? = null,

    @SerialName("currency")
    val currency: String? = null,

    @SerialName("prices")
    val prices: List<Double>? = null,

    @SerialName("reservation_rule_id")
    val reservationRuleId: Int? = null,

    @SerialName("time")
    val time: String? = null,

    @SerialName("show")
    val show: ShowRemote? = null
) {

    @Serializable
    data class ShowRemote(
        @SerialName("id")
        val id: Int? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("description")
        val description: String? = null,

        @SerialName("description_html")
        val descriptionHtml: String? = null,

        @SerialName("image")
        val image: String? = null,

        @SerialName("trailer")
        val trailer: String? = null
    )
}

