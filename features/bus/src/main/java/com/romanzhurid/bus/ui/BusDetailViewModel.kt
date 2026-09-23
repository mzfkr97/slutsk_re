package com.romanzhurid.bus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.bus.mapper.BusScheduleUiMapper
import com.romanzhurid.bus.model.BusScheduleUi
import com.romanzhurid.bus.ui.BusDetailViewModel.UiState
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.bus.interactor.BusScheduleInteractor
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

class BusDetailViewModel(
    private val busNumber: Int,
    private val busScheduleInteractor: BusScheduleInteractor,
    private val busScheduleUiMapper: BusScheduleUiMapper,
    private val dispatcherProvider: DispatcherProvider,
    progressDelegate: ProgressDelegate,
) : ViewModel(), UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(UiState(busNumber = busNumber)),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val busNumber: Int,
        val schedules: List<BusScheduleUi> = emptyList(),
        val isLoading: Boolean = true,
    )

    private val exceptionHandler = viewModelScope.exceptionHandler {
        updateUiState { it.copy(isLoading = false) }
    }

    init {
        observeSchedules()
    }

    private fun observeSchedules() {
        busScheduleInteractor
            .observeSchedulesByBusNumber(busNumber)
            .flowOn(dispatcherProvider.background())
            .map { schedules ->
                schedules.map(busScheduleUiMapper::map)

            }
            .onEach { mapped ->
                updateUiState {
                    it.copy(
                        schedules = mapped,
                        isLoading = false
                    )
                }
            }
            .catch { error ->
                exceptionHandler.handleException(EmptyCoroutineContext, error)
            }
            .launchIn(viewModelScope)
    }
}
