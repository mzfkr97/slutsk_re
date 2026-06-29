package com.romanzhurid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
    onBack: () -> Unit,
    entryDecorators: List<NavEntryDecorator<T>> =
        listOf(rememberSaveableStateHolderNavEntryDecorator()),
    entryProvider: (key: T) -> NavEntry<T>,
) {
    CompositionLocalProvider(LocalBackHandler provides onBack) {
        NavDisplay(
            backStack = backStack,
            modifier = modifier,
            onBack = onBack,
            entryDecorators = entryDecorators,
            transitionSpec = transitionSpec(),
            popTransitionSpec = popTransitionSpec(),
            predictivePopTransitionSpec = predictiveTransitionSpec(),
            entryProvider = entryProvider
        )
    }
}
