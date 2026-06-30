package com.romanzhurid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    entryDecorators: List<NavEntryDecorator<T>> =
        listOf(rememberSaveableStateHolderNavEntryDecorator()),
    entryProvider: (key: T) -> NavEntry<T>,
) {
    val backHandler = remember(onBack) { onBack }

    CompositionLocalProvider(
        LocalBackHandler provides backHandler
    ) {
        NavDisplay(
            modifier = modifier,
            backStack = backStack,
            onBack = {
                backHandler()
            },
            entryDecorators = entryDecorators,
            transitionSpec = transitionSpec(),
            popTransitionSpec = popTransitionSpec(),
            predictivePopTransitionSpec = predictiveTransitionSpec(),
            entryProvider = entryProvider
        )
    }
}