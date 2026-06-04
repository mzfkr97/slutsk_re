package com.romanzhurid.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.onboarding.di.OnboardingComponentDependenciesProvider
import com.romanzhurid.onboarding.di.OnboardingComponentHolder
import com.romanzhurid.onboarding.onboarding.OnboardingScreen
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalAppNavigator
import com.romanzhurid.navigation.composition.LocalBackHandler

@Composable
fun OnboardingFeatureHost(route: AppRoute.Onboarding) {
    val context = LocalContext.current.applicationContext
    val appNavigator = LocalAppNavigator.current
    val parentBack = LocalBackHandler.current

    val component = remember(route.instanceId) {
        OnboardingComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as OnboardingComponentDependenciesProvider)
                .onboardingComponentDependencies
        )
    }

    DisposableEffect(route.instanceId) {
        onDispose {
            OnboardingComponentHolder.clear(route.instanceId)
        }
    }

    val featureViewModel = viewModel<OnboardingFeatureHostViewModel>()
    val uiState by featureViewModel.collectUiState()
    val onBack: () -> Unit = {
        if (featureViewModel.handleBack().not()) {
            parentBack()
        }
    }
    AppNavDisplay(
        backStack = uiState.backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = onBack,
        entryProvider = entryProvider {
            entry<OnboardingFeatureRoute.Onboarding> {
                val viewModel = viewModel<OnboardingViewModel>(
                    factory = component.getOnboardingViewModelFactory()
                )
                OnboardingScreen(
                    viewModel = viewModel,
                    openFeatureRoute = featureViewModel::navigate,
                    openAppRoute = appNavigator::navigate,
                    onBack = onBack,
                )
            }
        }
    )
}
