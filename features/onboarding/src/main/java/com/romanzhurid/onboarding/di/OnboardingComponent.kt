package com.romanzhurid.onboarding.di

import com.romanzhurid.onboarding.onboarding.OnboardingViewModelFactory
import dagger.Component

@OnboardingScope
@Component(
    dependencies = [OnboardingComponentDependencies::class]
)
interface OnboardingComponent {
    fun getOnboardingViewModelFactory(): OnboardingViewModelFactory

    companion object {
        private var component: OnboardingComponent? = null

        fun get(): OnboardingComponent {
            return component ?: throw NotImplementedError("This component must be initialized")
        }
    }
}
