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
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalBackHandler
import com.romanzhurid.navigation.navigator.NavigatorImpl
import org.koin.compose.getKoin
import org.koin.core.scope.Scope

@Composable
fun <Route : AppRoute, FR : Any> FeatureHost(
    route: Route,
    featureScope: FeatureScope,
    initialStack: () -> List<FR>,
    entryProviderFactory: (Scope) -> (FR) -> NavEntry<FR>,
) {
    val parentBack = LocalBackHandler.current
    val koin = getKoin()

    val scope = remember(route.instanceId) {
        koin.getOrCreateScope(
            scopeId = route.instanceId,
            qualifier = featureScope.qualifier
        )
    }
    val navigator = remember(route) {
        NavigatorImpl(initialStack = initialStack())
    }

    DisposableEffect(scope) {
        onDispose {
            scope.close()
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

    val entryProvider = remember(route, scope) {
        entryProviderFactory(scope)
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
