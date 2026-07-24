package com.romanzhurid.navigation.host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.composition.LocalBackHandler
import com.romanzhurid.navigation.composition.LocalFeatureNavigator
import com.romanzhurid.navigation.navigator.rememberNavigator


@Composable
fun FeatureHost(
    key: Any,
    clearComponent: () -> Unit,
    initialStack: () -> List<NavKey>,
    entryProviderFactory: () -> (key: NavKey) -> NavEntry<NavKey>,
) {
    val parentBack = LocalBackHandler.current

    val navigator = rememberNavigator(initialStack())

    DisposableEffect(key) {
        onDispose { clearComponent() }
    }

    val onBack = remember(navigator, parentBack) {
        { navigator.back() || parentBack() }
    }

    val entryProvider = remember(key) {
        entryProviderFactory()
    }

    CompositionLocalProvider(LocalFeatureNavigator provides navigator) {
        AppNavDisplay(
            backStack = navigator.backStack,
            onBack = onBack,
            entryProvider = entryProvider
        )
    }
}
