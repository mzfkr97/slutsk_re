package com.romanzhurid.navigation.host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.composition.LocalBackHandler
import com.romanzhurid.navigation.navigator.NavigatorImpl

@Composable
fun <R : Any, FR : Any> FeatureHost(
    route: R,
    clearComponent: () -> Unit,
    initialStack: () -> List<FR>,
    entryProviderFactory: () -> (key: FR) -> NavEntry<FR>
) {
    val parentBack = LocalBackHandler.current

    val navigator = remember(route) {
        NavigatorImpl(initialStack = initialStack())
    }

    DisposableEffect(route) {
        onDispose {
            clearComponent()
        }
    }

    val backStack by remember(navigator) {
        derivedStateOf { navigator.backStack.toList() }
    }

    val onBack = remember(navigator, parentBack) {
        {
            navigator.back() || parentBack()
        }
    }

    val entryProvider = remember(route) {
        entryProviderFactory()
    }

    AppNavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider
    )
}
