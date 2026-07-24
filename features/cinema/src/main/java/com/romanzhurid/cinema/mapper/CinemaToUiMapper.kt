package com.romanzhurid.cinema.mapper

import com.romanzhurid.brandbook.ext.EMPTY_STRING
import com.romanzhurid.cinema.model.CinemaUiItem
import com.romanzhurid.common.ext.DatePattern
import com.romanzhurid.domain.cinema.Cinema
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CinemaToUiMapper @Inject constructor() {

    fun map(data: List<Cinema>): List<CinemaUiItem> {
        return data
            .groupBy { it.time.cinemaDateKey() }
            .toSortedMap()
            .flatMap { (_, items) ->

                val header = CinemaUiItem.CinemaDateUi(
                    date = items.first().time.cinemaDate()
                )

                val films = items.map(::mapMovie)

                listOf(header) + films
            }
    }

    private fun mapMovie(data: Cinema): CinemaUiItem.CinemaUi =
        with(data) {
            CinemaUiItem.CinemaUi(
                prices = "${prices.distinct().joinToString(", ")} $currency",
                time = time.cinemaTime(),
                cinemaId = cinemaId,
                descriptionHtml = descriptionHtml,
                imageUrl = imageUrl,
                name = name,
                trailerUrl = trailerUrl,
                dayNumber = time.cinemaDay()
            )
        }

    private fun String.cinemaDate(): String =
        runCatching {
            val date = parseCinemaDate()
            SimpleDateFormat(
                DatePattern.PATTERN__DD__MMMM_EEE,
                Locale.getDefault()
            ).format(date).replaceFirstChar {
                it.titlecase(Locale.getDefault())
            }
        }.getOrDefault(EMPTY_STRING)

    private fun String.cinemaDateKey(): String =
        runCatching {
            val date = parseCinemaDate()
            SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(date)
        }.getOrDefault(EMPTY_STRING)

    private fun String.cinemaTime(): String =
        runCatching {
            val date = parseCinemaDate()

            SimpleDateFormat(
                DatePattern.PATTERN__HH_MM,
                Locale.getDefault()
            ).format(date)
        }.getOrElse {
            EMPTY_STRING
        }

    private fun String.cinemaDay(): String =
        runCatching {
            parseCinemaDate().getDayNumber()
        }.getOrDefault(EMPTY_STRING)

    private fun String.parseCinemaDate(): Date =
        checkNotNull(
            SimpleDateFormat(
                DatePattern.PATTERN__yyyy_MM_dd_T_HH_mm_ss,
                Locale.ROOT
            ).parse(this)
        )

    private fun Date.getDayNumber(): String =
        SimpleDateFormat(
            DatePattern.PATTERN__dd,
            Locale.getDefault()
        ).format(this)
}
