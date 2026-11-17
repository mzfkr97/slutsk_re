package com.romanzhurid.home.mapper

import com.romanzhurid.domain.bus.model.BusEndPoint
import com.romanzhurid.home.model.StationUi

class BusStationsListUiMapper {

    fun map(list: BusEndPoint): StationUi {
        val routeName = buildString {
            if (!list.startStation.isNullOrEmpty()) {
                append(list.startStation)
            }
            if (!list.startStation.isNullOrEmpty() && !list.endStation.isNullOrEmpty()) {
                append(" - ")
            }
            if (!list.endStation.isNullOrEmpty()) {
                append(list.endStation)
            }
        }
        return StationUi(
            id = list.id,
            busNumber = list.busNumber,
            startStation = list.startStation,
            endStation = list.endStation,
            routeName = routeName,
            isFavorite = list.isFavorite
        )
    }
}