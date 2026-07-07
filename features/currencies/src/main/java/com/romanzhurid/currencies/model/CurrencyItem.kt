package com.romanzhurid.currencies.model

import androidx.compose.runtime.Immutable

sealed interface CurrencyItem {
    @Immutable
    data class Header(val title: String) : CurrencyItem
    @Immutable
    data class CurrencyUi(
        val id: Int,
        val abbreviation: String,
        val name: String,
        val officialRate: String,
        val isFavorite: Boolean
    ) : CurrencyItem
}
