package com.romanzhurid.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.host.FeatureHost
import com.romanzhurid.onboarding.di.OnboardingComponentHolder
import com.romanzhurid.onboarding.onboarding.OnboardingScreen
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel

@Composable
fun OnboardingFeatureHost(route: AppRoute.Onboarding) {
    val appNavigator = LocalNavigator.current

    FeatureHost(
        route = route.instanceId,
        clearComponent = {},
        initialStack = {
            listOf(OnboardingFeatureRoute.Onboarding)
        },
        entryProviderFactory = {
            entryProvider<OnboardingFeatureRoute> {
                entry<OnboardingFeatureRoute.Onboarding> {
                    val factory = OnboardingComponentHolder.getViewModelFactory()
                    val viewModel = viewModel<OnboardingViewModel>(
                        factory = factory
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
    )
}