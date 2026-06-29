package com.romanzhurid.onboarding.navigation

import androidx.lifecycle.ViewModel
import com.romanzhurid.navigation.navigator.NavigationDelegate
import com.romanzhurid.navigation.navigator.NavigationDelegateImpl

class OnboardingFeatureHostViewModel :
    ViewModel(),
    NavigationDelegate<OnboardingFeatureRoute> by NavigationDelegateImpl(
        initialStack = listOf(OnboardingFeatureRoute.Onboarding)
    )
