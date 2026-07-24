package com.romanzhurid.data.remote.cinema.mapper

import com.romanzhurid.data.remote.cinema.model.CinemaRemote
import com.romanzhurid.domain.cinema.Cinema
import javax.inject.Inject

class CinemaRemoteMapper @Inject constructor() {

    fun map(data: CinemaRemote) =
        with(data) {
            Cinema(
                prices = prices.orEmpty(),
                time = time.orEmpty(),
                cinemaId = show?.id ?: 0,
                currency = currency.orEmpty(),
                descriptionHtml = show?.descriptionHtml.orEmpty(),
                imageUrl = show?.image.orEmpty(),
                name = show?.name.orEmpty(),
                trailerUrl = show?.trailer.orEmpty(),
            )
        }
}
