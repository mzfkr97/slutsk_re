package com.romanzhurid.home.model

import com.romanzhurid.brandbook.R

enum class HomeBottomMenuType {
    SETTINGS,
    CURRENCIES,
    CINEMA,
    DELIVERY_FOOD,
    ROUTES
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
                        menuType = HomeBottomMenuType.CINEMA
                    )
                )
                add(
                    HomeBottomMenu(
                        titleResId = R.string.menu__delivery_food,
                        backgroundResId = R.drawable.img__main_delivery,
                        menuType = HomeBottomMenuType.DELIVERY_FOOD
                    )
                )
            }
        }
    }
}
