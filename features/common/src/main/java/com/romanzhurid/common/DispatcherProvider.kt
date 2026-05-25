package com.romanzhurid.common

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

interface DispatcherProvider {

    /**
     * A coroutine dispatcher that is confined to the Main thread operating with UI objects.
     */
    fun main(): CoroutineContext

    /**
     * Dispatcher for non-UI related work
     */
    fun background(): CoroutineContext
}

class DispatcherProviderImpl @Inject constructor() : DispatcherProvider {

    override fun main() = Dispatchers.Main

    override fun background() = Dispatchers.IO
}
