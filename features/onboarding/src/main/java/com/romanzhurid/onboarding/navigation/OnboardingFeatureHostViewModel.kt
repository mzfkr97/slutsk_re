package com.romanzhurid.onboarding.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.onboarding.navigation.OnboardingFeatureHostViewModel.UiState
import com.romanzhurid.navigation.navigator.NavigationStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OnboardingFeatureHostViewModel:
    ViewModel(),
    UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(
        UiState()
    ) {

    data class UiState(
        val backStack: List<OnboardingFeatureRoute> = emptyList(),
    )
    private val navigationStore = NavigationStore<OnboardingFeatureRoute>(
        initialStack = listOf(OnboardingFeatureRoute.Onboarding)
    )

    init {
        observeNavigation()
    }

    private fun observeNavigation() {
        navigationStore.backStack
            .onEach { stack ->
                updateUiState { it.copy(backStack = stack) }
            }
            .launchIn(viewModelScope)
    }

    fun navigate(route: OnboardingFeatureRoute) = navigationStore.navigate(route)

    fun clearAndPush(route: OnboardingFeatureRoute) = navigationStore.clearAndPush(route)

    fun handleBack(): Boolean = navigationStore.back()
}
