package com.romanzhurid.navigation.host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalBackHandler
import com.romanzhurid.navigation.composition.LocalFeatureNavigator
import com.romanzhurid.navigation.navigator.Navigator
import com.romanzhurid.navigation.navigator.rememberNavigator
import org.koin.compose.getKoin
import org.koin.core.scope.Scope

@Composable
fun <Route : AppRoute> FeatureHost(
    route: Route,
    featureScope: FeatureScope,
    initialStack: () -> List<NavKey>,
    entryProviderFactory: (Scope, Navigator<NavKey>) -> (NavKey) -> NavEntry<NavKey>,
) {
    val parentBack = LocalBackHandler.current
    val koin = getKoin()

    val scope = remember(route.instanceId) {
        koin.getOrCreateScope(
            scopeId = route.instanceId,
            qualifier = featureScope.qualifier
        )
    }
    val navigator = rememberNavigator(initialStack())

    DisposableEffect(scope) {
        onDispose {
            scope.close()
        }
    }

    val onBack = remember(navigator, parentBack) {
        { navigator.back() || parentBack() }
    }

    val entryProvider = remember(route, scope, navigator) {
        entryProviderFactory(scope, navigator)
    }

    CompositionLocalProvider(LocalFeatureNavigator provides navigator) {
        AppNavDisplay(
            backStack = navigator.backStack,
            onBack = onBack,
            entryProvider = entryProvider
        )
    }
}
