package com.romanzhurid.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.host.FeatureHost
import com.romanzhurid.onboarding.di.OnboardingComponentDependenciesProvider
import com.romanzhurid.onboarding.di.OnboardingComponentHolder
import com.romanzhurid.onboarding.onboarding.OnboardingScreen
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel

@Composable
fun OnboardingFeatureHost(route: AppRoute.Onboarding) {
    val context = LocalContext.current.applicationContext
    val appNavigator = LocalNavigator.current

    val component = remember(route.instanceId) {
        OnboardingComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as OnboardingComponentDependenciesProvider)
                .onboardingComponentDependencies
        )
    }

    FeatureHost(
        key = route.instanceId,
        clearComponent = {
            OnboardingComponentHolder.clear(route.instanceId)
        },
        initialStack = {
            listOf(OnboardingFeatureRoute.Onboarding)
        },
        entryProviderFactory = {
            entryProvider {
                entry<OnboardingFeatureRoute.Onboarding> {
                    val viewModel = viewModel<OnboardingViewModel>(
                        factory = component.getOnboardingViewModelFactory()
                    )
                    OnboardingScreen(
                        viewModel = viewModel,
                        onFinished = { destination ->
                            appNavigator.replace(destination)
                        }
                    )
                }
            }
        }
    )
}