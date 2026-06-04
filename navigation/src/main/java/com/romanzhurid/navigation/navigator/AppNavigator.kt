package com.romanzhurid.navigation.navigator

import com.romanzhurid.navigation.AppRoute

interface AppNavigator {
    fun navigate(route: AppRoute)
    fun replace(route: AppRoute)
    fun clearAndPush(route: AppRoute)
    fun back(): Boolean
}

class AppNavigatorImpl(private val store: NavigationStore<AppRoute>) : AppNavigator {

    override fun navigate(route: AppRoute) {
        store.navigate(route)
    }

    override fun replace(route: AppRoute) {
        store.replace(route)
    }

    override fun clearAndPush(route: AppRoute) {
        store.clearAndPush(route)
    }

    override fun back(): Boolean {
        return store.back()
    }
}
