package com.romanzhurid.navigation.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack


@Composable
fun rememberNavigator(initialStack: List<NavKey>): NavigatorImpl<NavKey> {
    val backStack = rememberNavBackStack(*initialStack.toTypedArray())
    return remember(backStack) { NavigatorImpl(backStack) }
}
