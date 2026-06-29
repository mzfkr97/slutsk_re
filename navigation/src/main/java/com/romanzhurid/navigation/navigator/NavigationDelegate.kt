package com.romanzhurid.navigation.navigator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.romanzhurid.navigation.Route as AppRoute

interface NavigationDelegate<R> {
    val backStack: StateFlow<List<R>>
    fun observeNavigation(scope: CoroutineScope, block: (List<R>) -> Unit): Job

    fun navigate(route: R)
    fun clearAndPush(route: R)
    fun clearAndPush(route: List<R>)
    fun back(): Boolean
}

class NavigationDelegateImpl<R : AppRoute>(initialStack: List<R>) : NavigationDelegate<R> {

    private val navigationStore = NavigationStore(
        initialStack = initialStack
    )

    override fun observeNavigation(
        scope: CoroutineScope,
        block: (List<R>) -> Unit): Job {
        return backStack
            .onEach { stack ->
                block.invoke(stack)
            }
            .launchIn(scope)
    }

    override val backStack: StateFlow<List<R>> =
        navigationStore.backStack

    override fun navigate(route: R) {
        navigationStore.navigate(route)
    }

    override fun clearAndPush(route: R) {
        navigationStore.clearAndPush(route)
    }

    override fun clearAndPush(route: List<R>) {
        navigationStore.clearAndPush(route)
    }

    override fun back(): Boolean {
        return navigationStore.back()
    }
}