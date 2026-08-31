package com.romanzhurid.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.host.FeatureHost
import com.romanzhurid.onboarding.onboarding.OnboardingScreen

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
                    OnboardingScreen(
                        onFinished = {
                            appNavigator.replace(AppRoute.Home())
                        }
                    )
                }
            }
        }
    )
}