package com.romanzhurid.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ExceptionsObserverImpl : ExceptionsFlow, ExceptionsEmitter {

    private val state = MutableSharedFlow<Throwable>()

    override fun subscribe(): Flow<Throwable> {
        return state.asSharedFlow()
    }

    override suspend fun emit(value: Throwable) {
        state.emit(value)
    }
}

typealias ExceptionsEmitter = FlowObserverEmitter<@JvmSuppressWildcards Throwable>
typealias ExceptionsFlow = FlowObserver<@JvmSuppressWildcards Throwable>
