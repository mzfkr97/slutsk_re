package com.romanzhurid.currencies.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.ext.EMPTY_STRING
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.currencies.presentation.CurrenciesViewModel.UiState
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import com.romanzhurid.domain.currencies.model.Currency
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class CurrenciesViewModel(
    private val currenciesInteractor: CurrenciesInteractor,
    private val res: ResourceProvider,
    private val dispatcherProvider: DispatcherProvider,
    progressDelegate: ProgressDelegate,
) : ViewModel(), UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(UiState()),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val others: List<Currency> = emptyList(),
        val favorites: List<Currency> = emptyList(),
        val lastUpdateTimeMs: String = EMPTY_STRING,
        val isLoading: Boolean = false
    )

    private val exceptionHandler = viewModelScope.exceptionHandler {
        updateUiState { it.copy(isLoading = false) }
    }
    var animatingCurrencyId by mutableStateOf<Int?>(null)

    init {
        observeCurrencies()
        loadCurrencies()
    }

    private fun observeCurrencies() {
        viewModelScope.launch(exceptionHandler) {
            currenciesInteractor
                .observeAllCurrencies()
                .collect { currencies ->
                    updateUiState {
                        it.copy(
                            others = currencies.filter { !it.isFavorite },
                            favorites = currencies.filter { it.isFavorite },
                            lastUpdateTimeMs = currencies.lastOrNull()?.date
                                ?: res.getString(R.string.currencies_update_time_unknown)
                        )
                    }
                }
        }
    }
    fun onToggleFavorite(id: Int) {
        animatingCurrencyId = id

        viewModelScope.launch {
            delay(450.milliseconds)
            currenciesInteractor.toggleFavorite(id)
            animatingCurrencyId = null
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
