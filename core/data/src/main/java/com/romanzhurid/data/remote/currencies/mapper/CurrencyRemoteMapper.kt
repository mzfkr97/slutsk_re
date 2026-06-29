package com.romanzhurid.data.remote.currencies.mapper

import com.romanzhurid.data.remote.currencies.model.BankCurrencyRemote
import com.romanzhurid.data.remote.currencies.model.CurrencyRemote
import com.romanzhurid.domain.currencies.model.Currency
import javax.inject.Inject

class CurrencyRemoteMapper @Inject constructor() {
    fun map(remote: CurrencyRemote): Currency = Currency(
        id = remote.curId,
        abbreviation = remote.abbreviation,
        name = remote.curName,
        officialRate = remote.officialRate,
        scale = remote.curScale,
        date = remote.date,
        isFavorite = false
    )

    fun map(remote: BankCurrencyRemote): Currency = Currency(
        id = remote.id,
        abbreviation = remote.abbreviation,
        name = remote.curName,
        officialRate = remote.officialRate,
        scale = remote.scale,
        date = remote.date,
        isFavorite = false
    )
}
