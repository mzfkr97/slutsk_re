package com.romanzhurid.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface ProgressState {
    data object Hide : ProgressState
    data class Show(
        val resId: Int,
        val onCancel: (() -> Unit)? = null
    ) : ProgressState
}

typealias ProgressEmitter = FlowObserverEmitter<@JvmSuppressWildcards ProgressState>
typealias ProgressFlow = FlowObserver<@JvmSuppressWildcards ProgressState>

class ProgressObserverImpl : ProgressFlow, ProgressEmitter {

    private val _state = MutableSharedFlow<ProgressState>()

    override fun subscribe(): Flow<ProgressState> {
        return _state.asSharedFlow()
    }

    override suspend fun emit(value: ProgressState) {
        _state.emit(value)
    }
}
