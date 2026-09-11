package com.romanzhurid.home.model

import com.romanzhurid.brandbook.R
import com.romanzhurid.common.ResourceProvider

data class MenuItemUi(
    val title: String,
    val imageResId: Int,
    val menuType: Type
) {
    enum class Type {
        CINEMA,
    }
}


fun getBottomMenuData(resourceProvider: ResourceProvider) = listOf(
    MenuItemUi(
        title = resourceProvider.getString(R.string.menu__cinema),
        imageResId = R.drawable.img__main_cinema,
        menuType = MenuItemUi.Type.CINEMA
    ),
)