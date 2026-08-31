package com.romanzhurid.onboarding.di

import com.romanzhurid.onboarding.onboarding.OnboardingViewModelFactory
import org.koin.core.context.GlobalContext

object OnboardingComponentHolder {
    fun getViewModelFactory(): OnboardingViewModelFactory {
        return GlobalContext.get().get()
    }
}