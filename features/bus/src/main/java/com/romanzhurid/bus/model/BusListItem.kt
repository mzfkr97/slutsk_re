package com.romanzhurid.bus.model

import androidx.compose.runtime.Immutable

sealed interface BusListItem {
    @Immutable
    data class Header(val title: String) : BusListItem

    @Immutable
    data class BusUi(
        val id: Int,
        val busNumber: Int,
        val startStation: String?,
        val endStation: String?,
        val isFavorite: Boolean
    ) : BusListItem
}
