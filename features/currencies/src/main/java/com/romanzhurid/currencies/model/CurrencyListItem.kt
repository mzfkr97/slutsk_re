package com.romanzhurid.currencies.model

import com.romanzhurid.domain.currencies.model.Currency

sealed interface CurrencyListItem {
    data class Header(val title: String) : CurrencyListItem
    data class Item(val currency: Currency) : CurrencyListItem
}
