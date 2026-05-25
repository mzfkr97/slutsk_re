package com.romanzhurid.navigation.featurehost

import android.util.Log
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.Route
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.StateFlow

interface NavigationDelegate<T : Route> {
    val backStack: StateFlow<List<T>>

    fun openFeatureRoute(destination: T)
    fun openAppRoute(destination: AppRoute)

    fun replaceBackStack(routes: List<T>)
    fun handleBack(): Boolean
    fun onBack()
}

class NavigationDelegateImpl<T : Route>(
    private val navigationChannelProvider: NavigationChannelProvider,
    initialRouteProvider: () -> List<T>
) :
    NavigationDelegate<T> {

    private val navigationStore = NavigationStore(
        initialStack = initialRouteProvider()
    )

    override val backStack: StateFlow<List<T>> = navigationStore.backStack

    override fun openFeatureRoute(destination: T) {
        navigationStore.navigate(destination)
    }

    override fun openAppRoute(destination: AppRoute) {
        navigationChannelProvider
            .navigationChannel
            .trySend(
                NavIntent.OpenFeature(destination)
            )
            .onFailure { Log.e("TAG", "Failed to navigate to $destination") }
    }

    override fun replaceBackStack(routes: List<T>) {
        navigationStore.replaceBackStack(routes)
    }

    override fun handleBack(): Boolean {
        return navigationStore.back()
    }

    override fun onBack() {
        if (handleBack().not()) {
            navigationChannelProvider
                .navigationChannel
                .trySend(NavIntent.Back)
                .onFailure { Log.e("TAG", "Failed to navigate") }
        }
    }
}
