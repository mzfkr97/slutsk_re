package com.romanzhurid.currencies.presentation

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
import com.romanzhurid.currencies.presentation.CurrenciesViewModel.UiState
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class CurrenciesViewModel(
    private val currenciesInteractor: CurrenciesInteractor,
    private val res: ResourceProvider,
    private val dispatcherProvider: DispatcherProvider,
    private val currencyUiMapper: CurrencyUiMapper,
    progressDelegate: ProgressDelegate,
) : ViewModel(), UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(UiState()),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val allCurrencies: List<CurrencyItem.CurrencyUi> = emptyList(),
        val currencies: List<CurrencyItem> = emptyList(),
        val searchQuery: String = EMPTY_STRING,
        val lastUpdateTimeMs: String = EMPTY_STRING,
        val isLoading: Boolean = false
    )

    private val exceptionHandler = viewModelScope.exceptionHandler {
        updateUiState { it.copy(isLoading = false) }
    }

    init {
        observeCurrencies()
        loadCurrencies()
    }

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
                    val onlyCurrencies = mapped.filterIsInstance<CurrencyItem.CurrencyUi>()

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

    private fun buildListWithHeaders(
        query: String,
        all: List<CurrencyItem.CurrencyUi>
    ): List<CurrencyItem> {

        val favorites = all.filter { it.isFavorite }
        val nonFavorites = all.filterNot { it.isFavorite }

        return buildList {
            if (favorites.isNotEmpty()) {
                add(CurrencyItem.Header("Favorites"))
                addAll(sortByQuery(favorites, query))
            }
            if (nonFavorites.isNotEmpty()) {
                add(CurrencyItem.Header("All currencies"))
                addAll(sortByQuery(nonFavorites, query))
            }
        }
    }

    private fun sortByQuery(
        list: List<CurrencyItem.CurrencyUi>,
        query: String
    ): List<CurrencyItem.CurrencyUi> {

        if (query.isBlank()) return list

        return list.sortedWith(
            compareByDescending { rank(it, query) }
        )
    }

    private fun rank(item: CurrencyItem.CurrencyUi, query: String): Int {
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

    fun onToggleFavorite(id: Int) {
        viewModelScope.launch {
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
}
