package com.romanzhurid.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost
import com.romanzhurid.onboarding.di.OnboardingFeatureScope
import com.romanzhurid.onboarding.onboarding.OnboardingScreen
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingFeatureHost(route: AppRoute.Onboarding) {
    FeatureHost(
        route = route,
        featureScope = OnboardingFeatureScope,
        initialStack = { listOf(OnboardingFeatureRoute.Onboarding) },
        entryProviderFactory = { scope ->
            entryProvider<OnboardingFeatureRoute> {
                entry<OnboardingFeatureRoute.Onboarding> {
                    val viewModel = koinViewModel<OnboardingViewModel>(scope = scope)
                    OnboardingScreen(viewModel)
                }
            }
        }
    )
}
