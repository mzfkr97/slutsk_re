package com.romanzhurid.cinema.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

sealed interface CinemaUiItem {

    @Stable
    data class CinemaDateUi(
        val date: String
    ) : CinemaUiItem

    @Immutable
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
