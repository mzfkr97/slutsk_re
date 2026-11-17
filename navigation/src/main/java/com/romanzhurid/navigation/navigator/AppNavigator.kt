package com.romanzhurid.navigation.navigator

interface Navigator<R> {
    val backStack: List<R>
    fun navigate(route: R)
    fun replace(route: R)
    fun setStack(routes: List<R>)
    fun back(): Boolean
}

class NavigatorImpl<R>(
    private val _backStack: MutableList<R>
) : Navigator<R> {

    private var lastNavigationTime = 0L

    override val backStack: List<R>
        get() = _backStack

    override fun navigate(route: R) {
        if (isDuplicate(route)) return

        _backStack.add(route)
        lastNavigationTime = System.currentTimeMillis()
    }

    private fun isDuplicate(route: R): Boolean {
        val now = System.currentTimeMillis()

        val tooFast = now - lastNavigationTime < 300
        val sameRoute = _backStack.lastOrNull() == route

        return tooFast && sameRoute
    }

    override fun replace(route: R) {
        if (_backStack.isEmpty()) {
            _backStack.add(route)
        } else {
            _backStack[_backStack.lastIndex] = route
        }
    }

    override fun setStack(routes: List<R>) {
        _backStack.clear()
        _backStack.addAll(routes)
    }

    override fun back(): Boolean {
        if (_backStack.size <= 1) return false

        _backStack.removeAt(_backStack.lastIndex)
        return true
    }
}
