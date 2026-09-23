package com.romanzhurid.bus.mapper

import com.romanzhurid.brandbook.R
import com.romanzhurid.bus.model.BusListItem
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.domain.bus.model.BusEndPoint

class BusUiMapper(private val res: ResourceProvider) {

    fun map(list: List<BusEndPoint>): List<BusListItem> {
        val favorites = list.filter { it.isFavorite }
        val others = list.filterNot { it.isFavorite }

        return buildList {
            if (favorites.isNotEmpty()) {
                add(BusListItem.Header(res.getString(R.string.bus__screen_favorites_header)))
                addAll(favorites.map(::map))
            }
            if (others.isNotEmpty()) {
                add(BusListItem.Header(res.getString(R.string.bus__screen_all_header)))
                addAll(others.map(::map))
            }
        }
    }

    private fun map(busEndPoint: BusEndPoint): BusListItem.BusUi {
        return with(busEndPoint) {
            BusListItem.BusUi(
                id = id,
                busNumber = busNumber,
                startStation = startStation,
                endStation = endStation,
                isFavorite = isFavorite
            )
        }
    }
}
