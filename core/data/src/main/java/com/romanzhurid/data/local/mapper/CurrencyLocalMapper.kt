package com.romanzhurid.data.local.mapper

import com.romanzhurid.data.local.entity.CurrencyEntity
import com.romanzhurid.domain.currencies.model.Currency
import javax.inject.Inject

class CurrencyLocalMapper @Inject constructor() {
    fun map(entity: CurrencyEntity): Currency {
        return Currency(
            id = entity.id,
            name = entity.name,
            abbreviation = entity.abbreviation,
            scale = entity.scale,
            officialRate = entity.officialRate,
            date = entity.date,
            isFavorite = entity.isFavorite
        )
    }

    fun mapToEntity(
        currency: Currency,
        isFavorite: Boolean
    ): CurrencyEntity {
        return with(currency) {
            CurrencyEntity(
                id = id,
                name = name,
                abbreviation = abbreviation,
                scale = scale,
                officialRate = officialRate,
                date = date,
                updatedAt = System.currentTimeMillis(),
                isFavorite = isFavorite
            )
        }
    }
}
