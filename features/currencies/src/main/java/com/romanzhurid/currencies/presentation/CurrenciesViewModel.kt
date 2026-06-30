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
        val currencies: List<CurrencyItem> = emptyList(),
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
                    updateUiState {
                        it.copy(
                            currencies = mapped,
                        )
                    }
                }
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
