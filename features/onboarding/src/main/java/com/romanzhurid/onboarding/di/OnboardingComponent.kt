package com.romanzhurid.onboarding.di

import com.romanzhurid.navigation.host.FeatureScope
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object OnboardingFeatureScope : FeatureScope {
    override val qualifier = named<OnboardingFeatureScope>()
}

val onboardingModule = module {
    scope<OnboardingFeatureScope> {
        viewModel {
            OnboardingViewModel(
                appSettings = get(),
                progressDelegate = get(),
            )
        }
    }
}
