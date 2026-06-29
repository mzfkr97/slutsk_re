package com.romanzhurid.domain.currencies.repository

import com.romanzhurid.domain.currencies.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getAllCurrencies(): List<Currency>
    suspend fun getCurrencyById(id: Int): Currency
    fun observeAllCurrencies(): Flow<List<Currency>>
    fun observeLastUpdateTime(): Flow<Long?>
    suspend fun refreshCurrencies()
    suspend fun toggleFavorite(id: Int)
}
