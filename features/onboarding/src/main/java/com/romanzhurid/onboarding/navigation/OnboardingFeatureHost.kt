package com.romanzhurid.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalBackHandler
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.navigator.NavigatorImpl
import com.romanzhurid.onboarding.di.OnboardingComponentDependenciesProvider
import com.romanzhurid.onboarding.di.OnboardingComponentHolder
import com.romanzhurid.onboarding.onboarding.OnboardingScreen
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel

@Composable
fun OnboardingFeatureHost(route: AppRoute.Onboarding) {
    val context = LocalContext.current.applicationContext
    val appNavigator = LocalNavigator.current
    val parentBack = LocalBackHandler.current

    val component = remember(route.instanceId) {
        OnboardingComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as OnboardingComponentDependenciesProvider)
                .onboardingComponentDependencies
        )
    }

    val navigator = remember(route.instanceId) {
        NavigatorImpl(
            initialStack = listOf(OnboardingFeatureRoute.Onboarding)
        )
    }

    DisposableEffect(route.instanceId) {
        onDispose {
            OnboardingComponentHolder.clear(route.instanceId)
        }
    }

    val backStack by remember(navigator) {
        derivedStateOf { navigator.backStack.toList() }
    }

    val onBack = remember(navigator, parentBack) {
        {
            if (navigator.back()) {
                true
            } else {
                parentBack()
            }
        }
    }

    val entryProvider = remember(component) {
        entryProvider<OnboardingFeatureRoute> {
            entry<OnboardingFeatureRoute.Onboarding> {
                val viewModel = viewModel<OnboardingViewModel>(
                    factory = component.getOnboardingViewModelFactory()
                )
                OnboardingScreen(
                    viewModel = viewModel,
                    onFinished = {
                        appNavigator.replace(AppRoute.Home())
                    }
                )
            }
        }
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
