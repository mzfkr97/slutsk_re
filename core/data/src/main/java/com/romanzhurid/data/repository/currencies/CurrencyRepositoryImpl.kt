package com.romanzhurid.data.repository.currencies

import com.romanzhurid.data.local.dao.CurrencyDao
import com.romanzhurid.data.local.mapper.CurrencyLocalMapper
import com.romanzhurid.data.remote.currencies.CurrencyApi
import com.romanzhurid.data.remote.currencies.mapper.CurrencyRemoteMapper
import com.romanzhurid.domain.currencies.model.Currency
import com.romanzhurid.domain.currencies.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApi,
    private val remoteMapper: CurrencyRemoteMapper,
    private val currencyDao: CurrencyDao,
    private val localMapper: CurrencyLocalMapper
) : CurrencyRepository {

    override suspend fun getAllCurrencies(): List<Currency> {
        return api.getAllCurrency().map(remoteMapper::map)
    }

    override suspend fun toggleFavorite(id: Int) {
        currencyDao.toggleFavorite(id)
    }

    override suspend fun getCurrencyById(id: Int): Currency {
        return remoteMapper.map(api.getCurrency(id))
    }

    override fun observeAllCurrencies(): Flow<List<Currency>> {
        return currencyDao.getAllCurrencies().map { entities ->
            entities.map(localMapper::map)
        }
    }

    override fun observeLastUpdateTime(): Flow<Long?> {
        return currencyDao.getLastUpdateTime()
    }

    override suspend fun refreshCurrencies() {
        val remoteData = api.getAllCurrency().map(remoteMapper::map)
        val favorites = currencyDao.getFavoriteIds()

        val entities = remoteData.map { currency ->
            localMapper.mapToEntity(
                currency = currency,
                isFavorite = currency.id in favorites
            )
        }

        currencyDao.deleteAllCurrencies()
        currencyDao.insertCurrencies(entities)
    }
}
