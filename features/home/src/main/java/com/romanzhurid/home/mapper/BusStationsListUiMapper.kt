package com.romanzhurid.home.mapper

import com.romanzhurid.domain.bus.model.BusEndPoint
import com.romanzhurid.home.model.StationUi

class BusStationsListUiMapper {

    fun map(list: BusEndPoint): StationUi {
        return StationUi(
            id = list.id,
            busNumber = list.busNumber,
            isFavorite = list.isFavorite
        )
    }
}