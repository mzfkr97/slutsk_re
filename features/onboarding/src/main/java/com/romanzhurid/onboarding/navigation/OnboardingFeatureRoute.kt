package com.romanzhurid.onboarding.navigation

import com.romanzhurid.navigation.FeatureRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface OnboardingFeatureRoute : FeatureRoute {

    @Serializable
    data object Onboarding : OnboardingFeatureRoute
}
