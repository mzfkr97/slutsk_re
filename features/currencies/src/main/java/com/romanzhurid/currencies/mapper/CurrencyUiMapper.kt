package com.romanzhurid.currencies.mapper

import com.romanzhurid.brandbook.R
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.currencies.model.CurrencyItem
import com.romanzhurid.domain.currencies.model.Currency
import javax.inject.Inject

class CurrencyUiMapper @Inject constructor(private val res: ResourceProvider) {

    fun map(currency: List<Currency>): List<CurrencyItem> {
        val favorites = currency.filter { it.isFavorite }
        val others = currency.filterNot { it.isFavorite }

        return buildList {
            if (favorites.isNotEmpty()) {
                add(CurrencyItem.Header(res.getString(R.string.currencies__screen_favorites_header)))
                addAll(favorites.map(::map))
            }
            if (others.isNotEmpty()) {
                add(CurrencyItem.Header(res.getString(R.string.currencies__screen_all_currencies_header)))
                addAll(others.map(::map))
            }
        }
    }

    private fun map(currency: Currency): CurrencyItem.CurrencyUi {
        return with(currency) {
            CurrencyItem.CurrencyUi(
                id = id,
                abbreviation = "$scale $abbreviation",
                name = name,
                officialRate = "%.4f".format(officialRate),
                isFavorite = isFavorite
            )
        }
    }
}