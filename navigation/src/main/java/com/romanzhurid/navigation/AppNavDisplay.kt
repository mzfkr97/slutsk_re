package com.romanzhurid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.romanzhurid.navigation.animation.popTransitionSpec
import com.romanzhurid.navigation.animation.predictiveTransitionSpec
import com.romanzhurid.navigation.animation.transitionSpec
import com.romanzhurid.navigation.composition.LocalBackHandler

@Composable
fun <T : Any> AppNavDisplay(
    modifier: Modifier = Modifier,
    backStack: List<T>,
    onBack: () -> Boolean,
    entryProvider: (key: T) -> NavEntry<T>,
) {
    CompositionLocalProvider(
        LocalBackHandler provides onBack
    ) {
        NavDisplay(
            modifier = modifier,
            backStack = backStack,
            onBack = {
                onBack()
            },
            entryDecorators = navEntry(),
            transitionSpec = transitionSpec(),
            popTransitionSpec = popTransitionSpec(),
            predictivePopTransitionSpec = predictiveTransitionSpec(),
            entryProvider = entryProvider
        )
    }
}

@Composable
private fun <T : Any> navEntry() : List<NavEntryDecorator<T>> {
    return listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    )
}