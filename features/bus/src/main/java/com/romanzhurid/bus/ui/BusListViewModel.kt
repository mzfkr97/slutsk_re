package com.romanzhurid.bus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.ext.EMPTY_STRING
import com.romanzhurid.bus.mapper.BusUiMapper
import com.romanzhurid.bus.model.BusListItem
import com.romanzhurid.bus.model.BusListItem.BusUi
import com.romanzhurid.bus.ui.BusListViewModel.Event
import com.romanzhurid.bus.ui.BusListViewModel.UiState
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.bus.interactor.BusEndPointInteractor
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext

class BusListViewModel(
    private val busEndPointInteractor: BusEndPointInteractor,
    private val busUiMapper: BusUiMapper,
    private val dispatcherProvider: DispatcherProvider,
    progressDelegate: ProgressDelegate,
) : ViewModel(),
    UiStateDelegate<UiState, Event> by UiStateDelegateImpl(UiState()),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val allBuses: List<BusUi> = emptyList(),
        val buses: List<BusListItem> = emptyList(),
        val searchQuery: String = EMPTY_STRING,
    )

    sealed interface Event {
        data class NavigateToDetail(val busNumber: Int) : Event
    }

    private val exceptionHandler = viewModelScope.exceptionHandler()

    init {
        observeBuses()
    }

    private fun observeBuses() {
        busEndPointInteractor
            .observeAll()
            .map { buses ->
                withContext(dispatcherProvider.background()) {
                    busUiMapper.map(buses)
                }
            }
            .onEach { buses ->
                val onlyBuses = buses.filterIsInstance<BusUi>()
                updateUiState {
                    it.copy(
                        allBuses = onlyBuses,
                        buses = buses
                    )
                }
            }
            .catch { error ->
                exceptionHandler.handleException(EmptyCoroutineContext, error)
            }
            .launchIn(viewModelScope)
    }

    fun onToggleFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        viewModelScope.launch(exceptionHandler) {
            withContext(dispatcherProvider.background()) {
                busEndPointInteractor.toggleFavorite(
                    id = id,
                    isFavorite = isFavorite.not()
                )
            }
        }
    }

    fun onBusClicked(busNumber: Int) {
        viewModelScope.sendEvent(Event.NavigateToDetail(busNumber))
    }

    fun searchItem(query: String) {
        updateUiState {
            it.copy(
                searchQuery = query,
                buses = filterByQuery(
                    all = it.allBuses,
                    query = query
                )
            )
        }
    }

    private fun filterByQuery(
        all: List<BusUi>,
        query: String
    ): List<BusListItem> {
        if (query.isBlank()) return all

        val q = query.lowercase()
        return all.filter { bus ->
            bus.busNumber.toString().contains(q) ||
                bus.startStation?.lowercase()?.contains(q) == true ||
                bus.endStation?.lowercase()?.contains(q) == true
        }
    }
}
