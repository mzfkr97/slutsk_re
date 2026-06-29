package com.romanzhurid.onboarding.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.navigation.navigator.NavigationDelegate
import com.romanzhurid.navigation.navigator.NavigationDelegateImpl
import com.romanzhurid.onboarding.navigation.OnboardingFeatureHostViewModel.UiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OnboardingFeatureHostViewModel :
    ViewModel(),
    UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(
        UiState()
    ),
    NavigationDelegate<OnboardingFeatureRoute> by NavigationDelegateImpl(
        initialStack = listOf(OnboardingFeatureRoute.Onboarding)
    ) {

    data class UiState(
        val backStack: List<OnboardingFeatureRoute> = emptyList(),
    )

    init {
        observeNavigation()
    }

    private fun observeNavigation() {
        backStack
            .onEach { stack ->
                updateUiState { it.copy(backStack = stack) }
            }
            .launchIn(viewModelScope)
    }

    fun handleBack(): Boolean = back()
}
