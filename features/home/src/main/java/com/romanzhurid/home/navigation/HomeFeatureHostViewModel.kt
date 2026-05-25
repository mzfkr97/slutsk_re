package com.romanzhurid.home.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.home.navigation.HomeFeatureHostViewModel.UiState
import com.romanzhurid.navigation.featurehost.NavigationChannelProvider
import com.romanzhurid.navigation.featurehost.NavigationDelegate
import com.romanzhurid.navigation.featurehost.NavigationDelegateImpl
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeFeatureHostViewModel(navigationChannelProvider: NavigationChannelProvider):
    ViewModel(),
    NavigationDelegate<HomeFeatureRoute> by NavigationDelegateImpl(
        navigationChannelProvider = navigationChannelProvider,
        initialRouteProvider = {
            listOf(HomeFeatureRoute.Home())
        }
    ),
    UiStateDelegate<UiState, Unit> by UiStateDelegateImpl(
        UiState()
    ) {

    data class UiState(
        val backStack: List<HomeFeatureRoute> = emptyList(),
    )

    init {
        backStack
            .onEach { stack ->
                updateUiState { it.copy(backStack = stack) }
            }
            .launchIn(viewModelScope)
    }
}
