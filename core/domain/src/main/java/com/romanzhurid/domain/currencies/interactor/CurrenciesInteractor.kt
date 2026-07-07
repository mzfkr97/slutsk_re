package com.romanzhurid.domain.currencies.interactor

import com.romanzhurid.domain.currencies.model.Currency
import com.romanzhurid.domain.currencies.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrenciesInteractor @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend fun getAllCurrencies(): List<Currency> {
        return repository.getAllCurrencies()
    }

    suspend fun toggleFavorite(id: Int) {
        repository.toggleFavorite(id)
    }


    suspend fun getCurrencyById(id: Int): Currency {
        return repository.getCurrencyById(id)
    }

    fun observeAllCurrencies(): Flow<List<Currency>> {
        return repository.observeAllCurrencies()
    }

    fun observeLastUpdateTime(): Flow<Long?> {
        return repository.observeLastUpdateTime()
    }

    suspend fun refreshCurrencies() {
        repository.refreshCurrencies()
    }
}
