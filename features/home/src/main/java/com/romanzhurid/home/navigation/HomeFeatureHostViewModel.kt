package com.romanzhurid.home.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.home.navigation.HomeFeatureHostViewModel.UiState
import com.romanzhurid.navigation.navigator.NavigationStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeFeatureHostViewModel:
    ViewModel(),
    UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(
        UiState()
    ) {

    data class UiState(
        val backStack: List<HomeFeatureRoute> = emptyList(),
    )
    private val navigationStore = NavigationStore<HomeFeatureRoute>(
        initialStack = listOf(HomeFeatureRoute.Home)
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

    fun navigate(route: HomeFeatureRoute) = navigationStore.navigate(route)

    fun clearAndPush(route: HomeFeatureRoute) = navigationStore.clearAndPush(route)

    fun handleBack(): Boolean = navigationStore.back()
}
