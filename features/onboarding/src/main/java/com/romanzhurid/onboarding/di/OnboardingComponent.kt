package com.romanzhurid.onboarding.di

import com.romanzhurid.onboarding.onboarding.OnboardingViewModelFactory
import org.koin.dsl.module

val onboardingModule = module {
    factory<OnboardingViewModelFactory> {
        OnboardingViewModelFactory(
            appSettings = get(),
            progressDelegate = get(),
        )
    }
}
