package com.romanzhurid.common

import kotlinx.coroutines.flow.Flow

interface FlowObserver<out T> {
    fun subscribe(): Flow<T>
}

interface FlowObserverEmitter<in T> {
    suspend fun emit(value: T)
}
