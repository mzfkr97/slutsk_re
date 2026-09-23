package com.romanzhurid.bus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.R
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration.Companion.milliseconds

class BusListViewModel(
    private val busEndPointInteractor: BusEndPointInteractor,
    private val busUiMapper: BusUiMapper,
    private val dispatcherProvider: DispatcherProvider,
    progressDelegate: ProgressDelegate,
) : ViewModel(),
    UiStateDelegate<UiState, Event> by UiStateDelegateImpl(
        UiState(
            busState = BusState.Success(emptyList())
        )
    ),
    ProgressDelegate by progressDelegate {

    sealed interface BusState {
        data class Success(val buses: List<BusListItem>) : BusState
        data class Error(val message: Int) : BusState
    }

    data class UiState(
        val allBuses: List<BusUi> = emptyList(),
        val busState: BusState,
        val searchQuery: String = EMPTY_STRING,
    )

    sealed interface Event {
        data class NavigateToDetail(val busNumber: Int) : Event
    }

    private val exceptionHandler = viewModelScope.exceptionHandler()

    init {
        observeBuses()
    }

    @OptIn(FlowPreview::class)
    private fun observeBuses() {
        busEndPointInteractor
            .observeAll()
            .map(busUiMapper::map)
            .flowOn(dispatcherProvider.background())
            .onEach { buses ->
                val onlyBuses = buses.filterIsInstance<BusUi>()
                updateUiState {
                    it.copy(
                        allBuses = onlyBuses,
                        busState = BusState.Success(buses)
                    )
                }
            }
            .catch { error ->
                updateUiState {
                    it.copy(
                        busState = BusState.Error(R.string.error__default_massage)
                    )
                }
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
                busState = BusState.Success(
                    buses = filterByQuery(
                        all = it.allBuses,
                        query = query
                    )
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
