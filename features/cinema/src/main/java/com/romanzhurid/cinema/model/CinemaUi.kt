package com.romanzhurid.cinema.model

sealed interface CinemaUiItem {

    data class CinemaDateUi(
        val date: String
    ) : CinemaUiItem

    data class CinemaUi(
        val prices: String,
        val time: String,
        val cinemaId: Int,
        val descriptionHtml: String,
        val imageUrl: String,
        val name: String,
        val trailerUrl: String,
        val dayNumber: String
    ) : CinemaUiItem
}
