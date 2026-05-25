package com.romanzhurid.navigation.featurehost

import com.romanzhurid.navigation.BackStackStrategy.ADD
import com.romanzhurid.navigation.BackStackStrategy.CLEAR
import com.romanzhurid.navigation.BackStackStrategy.REPLACE
import com.romanzhurid.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class NavigationStore<T : Route>(initialStack: List<T>) {

    private val _backStack = MutableStateFlow(initialStack.toList())
    val backStack: StateFlow<List<T>> = _backStack

    internal fun replaceBackStack(routes: List<T>) {
        _backStack.update { routes }
    }

    internal fun navigate(destination: T) {
        _backStack.update { currentStack ->
            when (destination.backStackStrategy) {
                ADD -> {
                    if (currentStack.lastOrNull() == destination) {
                        currentStack
                    } else {
                        currentStack + destination
                    }
                }
                REPLACE -> {
                    if (currentStack.isEmpty()) {
                        listOf(destination)
                    } else {
                        currentStack.dropLast(1) + destination
                    }
                }
                CLEAR -> {
                    listOf(destination)
                }
            }
        }
    }

    internal fun back(): Boolean {
        val currentStack = _backStack.value

        return if (currentStack.size <= 1) {
            false
        } else {
            _backStack.update { currentStack.dropLast(1) }
            true
        }
    }
}
