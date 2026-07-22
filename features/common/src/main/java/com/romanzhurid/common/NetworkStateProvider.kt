package com.romanzhurid.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.romanzhurid.brandbook.ext.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * listening for network changes on device and emitting them as a NetworkState
 *
 * @return [stateFlow] state for network status connections
 */
interface NetworkStateFlow {

    fun subscribeNetworkChanges(): Flow<NetworkStatusState>
}

sealed class NetworkStatusState {

    object Connected : NetworkStatusState()

    object Disconnected : NetworkStatusState()
}

class NetworkStateProvider @Inject constructor(
    private val context: Context
) : NetworkStateFlow {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    private val state = MutableStateFlow(currentNetwork)

    init {
        state.subscriptionCount
            .map { count ->
                count > 0
            }
            .distinctUntilChanged()
            .onEach { isActive ->
                if (isActive) subscribe() else unsubscribe()
            }
            .launchIn(CoroutineScope(SupervisorJob() + Dispatchers.Main))
    }

    private val currentNetwork: NetworkStatusState
        get() = if (context.isNetworkAvailable()) {
            NetworkStatusState.Connected
        } else {
            NetworkStatusState.Disconnected
        }

    override fun subscribeNetworkChanges(): Flow<NetworkStatusState> = state.asStateFlow()

    private fun subscribe() {
        if (networkCallback != null) return

        networkCallback = NetworkCallbackImpl().also {
            connectivityManager.registerDefaultNetworkCallback(it)
        }

        emitNetworkState(currentNetwork)
    }

    private fun unsubscribe() {
        if (networkCallback == null) return

        networkCallback?.run {
            connectivityManager.unregisterNetworkCallback(this)
        }
        networkCallback = null
    }

    private fun emitNetworkState(newState: NetworkStatusState) {
        state.tryEmit(newState)
    }

    private inner class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) = emitNetworkState(NetworkStatusState.Connected)

        override fun onLost(network: Network) = emitNetworkState(NetworkStatusState.Disconnected)
    }
}
