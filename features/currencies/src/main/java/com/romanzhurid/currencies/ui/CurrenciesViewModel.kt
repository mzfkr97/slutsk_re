package com.romanzhurid.currencies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.ext.EMPTY_STRING
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.currencies.mapper.CurrencyUiMapper
import com.romanzhurid.currencies.model.CurrencyItem
import com.romanzhurid.currencies.model.CurrencyItem.CurrencyUi
import com.romanzhurid.currencies.ui.CurrenciesViewModel.UiState
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrenciesViewModel(
    private val currenciesInteractor: CurrenciesInteractor,
    private val res: ResourceProvider,
    private val dispatcherProvider: DispatcherProvider,
    private val currencyUiMapper: CurrencyUiMapper,
    progressDelegate: ProgressDelegate,
) : ViewModel(), UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(UiState()),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val allCurrencies: List<CurrencyUi> = emptyList(),
        val currencies: List<CurrencyItem> = emptyList(),
        val searchQuery: String = EMPTY_STRING,
        val lastUpdateTimeMs: String = EMPTY_STRING,
        val isLoading: Boolean = false,

    )

    private val exceptionHandler = viewModelScope.exceptionHandler {
        updateUiState { it.copy(isLoading = false) }
    }

    init {
        observeCurrencies()
        loadCurrencies()
    }

    private fun observeCurrencies() {
        viewModelScope.launch(exceptionHandler) {
            currenciesInteractor
                .observeAllCurrencies()
                .map { currencies ->
                    withContext(dispatcherProvider.background()) {
                        currencyUiMapper.map(currencies)
                    }
                }
                .collect { mapped ->
                    val onlyCurrencies = mapped.filterIsInstance<CurrencyUi>()

                    updateUiState {
                        it.copy(
                            allCurrencies = onlyCurrencies,
                            currencies = buildListWithHeaders(
                                query = it.searchQuery,
                                all = onlyCurrencies
                            )
                        )
                    }
                }
        }
    }

    fun onToggleFavorite(id: Int) {
        viewModelScope.launch(exceptionHandler)  {
            withContext(dispatcherProvider.background()) {
                currenciesInteractor.toggleFavorite(id)
            }
        }
    }

    fun loadCurrencies() {
        viewModelScope.launch(exceptionHandler) {
            updateUiState { it.copy(isLoading = true) }
            withContext(dispatcherProvider.background()) {
                currenciesInteractor.refreshCurrencies()
            }
            updateUiState { it.copy(isLoading = false) }
        }
    }

    // region SEARCH AREA
    fun searchItem(query: String) {
        viewModelScope.launch(exceptionHandler) {
            val newList = buildListWithHeaders(
                query = query,
                all = stateValue.allCurrencies
            )

            updateUiState {
                it.copy(
                    searchQuery = query,
                    currencies = newList
                )
            }
        }
    }

    private fun buildListWithHeaders(
        query: String,
        all: List<CurrencyUi>
    ): List<CurrencyItem> {
        val favorites = all.filter { it.isFavorite }
        val nonFavorites = all.filterNot { it.isFavorite }
        return buildList {
            if (favorites.isNotEmpty()) {
                add(CurrencyItem.Header(res.getString(R.string.currencies__screen_favorites_header)))
                addAll(sortByQuery(favorites, query))
            }
            if (nonFavorites.isNotEmpty()) {
                add(CurrencyItem.Header(res.getString(R.string.currencies__screen_all_currencies_header)))
                addAll(sortByQuery(nonFavorites, query))
            }
        }
    }

    private fun sortByQuery(
        list: List<CurrencyUi>,
        query: String
    ): List<CurrencyUi> {
        if (query.isBlank()) return list
        return list.sortedWith(compareByDescending { rank(it, query) })
    }

    private fun rank(
        item: CurrencyUi,
        query: String
    ): Int {
        val q = query.lowercase()
        val name = item.name.lowercase()
        val code = item.abbreviation.lowercase()

        return when {
            code.startsWith(q) -> 3
            name.startsWith(q) -> 2
            code.contains(q) || name.contains(q) -> 1
            else -> 0
        }
    }
    // endregion
}
