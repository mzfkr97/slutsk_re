package com.romanzhurid.data.repository.cinema

import com.romanzhurid.data.remote.cinema.ApiCinema
import com.romanzhurid.data.remote.cinema.mapper.CinemaRemoteMapper
import com.romanzhurid.domain.cinema.Cinema
import com.romanzhurid.domain.cinema.repo.CinemaRepository

class CinemaRepositoryImpl (
    private val apiCinema: ApiCinema,
    private val cinemaRemoteMapper: CinemaRemoteMapper
) : CinemaRepository {

    override suspend fun getAllCinema(
        timeStart: String,
        timeEnd: String
    ): List<Cinema> =
        apiCinema.getCinema(
            timeStart = timeStart,
            timeEnd = timeEnd
        ).map(cinemaRemoteMapper::map)
}
