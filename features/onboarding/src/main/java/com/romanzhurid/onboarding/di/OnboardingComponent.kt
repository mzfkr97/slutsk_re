package com.romanzhurid.onboarding.di

import com.romanzhurid.onboarding.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    viewModel {
        OnboardingViewModel(
            appSettings = get(),
            progressDelegate = get(),
        )
    }
}
