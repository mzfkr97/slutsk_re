package com.romanzhurid.onboarding.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.domain.AppSettings

class OnboardingViewModelFactory(
    private val appSettings: AppSettings,
    private val progressDelegate: ProgressDelegate,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnboardingViewModel(
            appSettings = appSettings,
            progressDelegate = progressDelegate,
        ) as T
    }
}
