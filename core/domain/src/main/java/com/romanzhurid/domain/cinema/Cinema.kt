package com.romanzhurid.domain.cinema

data class Cinema(
    val prices: List<Double>,
    val time: String,
    val cinemaId: Int,
    val currency: String,
    val descriptionHtml: String,
    val imageUrl: String,
    val name: String,
    val trailerUrl: String,
)
