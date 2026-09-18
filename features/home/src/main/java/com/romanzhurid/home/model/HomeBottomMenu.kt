package com.romanzhurid.home.model

import com.romanzhurid.brandbook.R

sealed interface HomeBottomMenuType {
    data object Settings : HomeBottomMenuType
    data object Currencies : HomeBottomMenuType
    data object Cinema : HomeBottomMenuType
    data object DeliveryFood : HomeBottomMenuType
    data class BusRoutes(val busNumber: Int? = null) : HomeBottomMenuType
}

data class HomeBottomMenu(
    val titleResId: Int,
    val backgroundResId: Int,
    val menuType: HomeBottomMenuType
) {

    companion object {
        fun getDefaultMenu() : List<HomeBottomMenu> {
            return buildList {
                add(
                    HomeBottomMenu(
                        titleResId = R.string.menu__cinema,
                        backgroundResId = R.drawable.img__main_cinema,
                        menuType = HomeBottomMenuType.Cinema
                    )
                )
                add(
                    HomeBottomMenu(
                        titleResId = R.string.menu__delivery_food,
                        backgroundResId = R.drawable.img__main_delivery,
                        menuType = HomeBottomMenuType.DeliveryFood
                    )
                )
            }
        }
    }
}
