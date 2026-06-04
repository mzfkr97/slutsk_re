package com.romanzhurid.onboarding.di

object OnboardingComponentHolder {
    private val components = mutableMapOf<String, OnboardingComponent>()

    fun get(
        instanceId: String,
        dependencies: OnboardingComponentDependencies
    ): OnboardingComponent {
        return components.getOrPut(instanceId) {
            DaggerOnboardingComponent.builder()
                .onboardingComponentDependencies(dependencies)
                .build()
        }
    }

    fun clear(instanceId: String) {
        components.remove(instanceId)
    }
}