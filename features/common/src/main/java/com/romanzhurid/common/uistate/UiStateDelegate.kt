package com.romanzhurid.common.uistate

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UiState - must be Data class, immutable
 */
interface UiStateDelegate<UiState, Event> {

    /**
     * Declarative description of the UI based on the current state.
     */
    val uiState: Flow<UiState>

    val singleEvents: Flow<Event>

    /**
     * State is read-only
     * The only way to change the state is to emit[updateUiState] an action,
     * an object describing what happened.
     */
    val stateValue: UiState

    suspend fun UiStateDelegate<UiState, Event>.sendEvent(event: Event)

    fun CoroutineScope.sendEvent(event: Event)

    /**
     * Reduce are functions that take the current state and an action as arguments,
     * and changed a new state result. In other words, (state: ViewState) => newState.
     */
    fun UiStateDelegate<UiState, Event>.updateUiState(
        transform: (state: UiState) -> UiState
    )
}

class UiStateDelegateImpl<UiState, Event>(
    initialViewState: UiState,
    singleLiveEventCapacity: Int = Channel.BUFFERED,
) : UiStateDelegate<UiState, Event> {

    private val stateFlow = MutableStateFlow(initialViewState)

    override val uiState: Flow<UiState>
        get() = stateFlow.asStateFlow()

    override val stateValue: UiState
        get() = stateFlow.value

    private val singleEventsChannel = Channel<Event>(singleLiveEventCapacity)

    override val singleEvents: Flow<Event>
        get() = singleEventsChannel.receiveAsFlow()

    override suspend fun UiStateDelegate<UiState, Event>.sendEvent(event: Event) {
        singleEventsChannel.send(event)
    }

    override fun CoroutineScope.sendEvent(event: Event) {
        launch {
            singleEventsChannel.send(event)
        }
    }

    override fun UiStateDelegate<UiState, Event>.updateUiState(
        transform: (state: UiState) -> UiState
    ) {
        stateFlow.update { current ->
            transform(current)
        }
    }
}
