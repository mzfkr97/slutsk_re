package com.romanzhurid.domain.cinema.repo

import com.romanzhurid.domain.cinema.Cinema

interface CinemaRepository {
    suspend fun getAllCinema(timeStart: String, timeEnd: String): List<Cinema>
}
