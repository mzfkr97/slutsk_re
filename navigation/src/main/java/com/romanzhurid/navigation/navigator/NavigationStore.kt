package com.romanzhurid.navigation.navigator

import com.romanzhurid.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NavigationStore<T : Route>(initialStack: List<T>) {

    private val _backStack = MutableStateFlow(initialStack.toList())
    val backStack: StateFlow<List<T>> = _backStack

    fun navigate(route: T) {
        _backStack.update { it + route }
    }

    fun replace(route: T) {
        _backStack.update { current ->
            if (current.isEmpty()) {
                listOf(route)
            } else {
                current.dropLast(1) + route
            }
        }
    }

    fun clearAndPush(route: T) {
        _backStack.update { listOf(route) }
    }

    fun clearAndPush(route: List<T>) {
        _backStack.update { route }
    }

    fun back(): Boolean {
        val current = _backStack.value
        return if (current.size > 1) {
            _backStack.value = current.dropLast(1)
            true
        } else {
            false
        }
    }
}
