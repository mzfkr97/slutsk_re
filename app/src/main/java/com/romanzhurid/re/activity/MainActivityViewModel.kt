package com.romanzhurid.re.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState
import com.romanzhurid.common.ExceptionsFlow
import com.romanzhurid.common.ProgressFlow
import com.romanzhurid.common.ProgressState
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.AppSettings
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.BackStackStrategy
import com.romanzhurid.navigation.featurehost.NavIntent
import com.romanzhurid.navigation.featurehost.NavigationChannelProvider
import com.romanzhurid.navigation.featurehost.NavigationDelegate
import com.romanzhurid.navigation.featurehost.NavigationDelegateImpl
import com.romanzhurid.re.activity.MainActivityViewModel.Event
import com.romanzhurid.re.activity.MainActivityViewModel.UiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class MainActivityViewModel(
    private val appSettings: AppSettings,
    private val progressFlow: ProgressFlow,
    private val exceptionsFlow: ExceptionsFlow,
    private val navigationChannelProvider: NavigationChannelProvider,
    res: ResourceProvider,
) : ViewModel(),
    NavigationDelegate<AppRoute> by NavigationDelegateImpl(
        navigationChannelProvider = navigationChannelProvider,
        initialRouteProvider = { emptyList()}
    ),
    UiStateDelegate<UiState, Event> by UiStateDelegateImpl(UiState()),
    ResourceProvider by res {

    data class UiState(
        val backStack: List<AppRoute>? = null,
        val progressState: ProgressState = ProgressState.Hide,
        val errorState: ErrorState? = null
    )

    sealed interface Event {
        data object Finish : Event
    }

    init {
        replaceBackStack(resolveBackStack())
        observeNavigation()
        observeProgress()
        observeExceptions()
        observeNavigationChannel()
    }

    private fun resolveBackStack(): List<AppRoute> {
        return when {
            appSettings.isFirstAppStart -> {
                listOf(AppRoute.Home(backStackStrategy = BackStackStrategy.CLEAR))
            }
            else -> {
                listOf(AppRoute.Home(backStackStrategy = BackStackStrategy.CLEAR))
            }
        }
    }

    private fun observeNavigation() {
        backStack
            .onEach { stack ->
                updateUiState { it.copy(backStack = stack) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeNavigationChannel() {
        navigationChannelProvider
            .navigationChannel
            .receiveAsFlow()
            .onEach { destination ->
                when (destination) {
                    is NavIntent.OpenFeature -> {
                        openFeatureRoute(destination.appRoute)
                    }
                    NavIntent.Back -> {
                        activityBack()
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeProgress() {
        progressFlow
            .subscribe()
            .onEach { state ->
                updateUiState { it.copy(progressState = state) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeExceptions() {
        exceptionsFlow
            .subscribe()
            .onEach(::handleException)
            .launchIn(viewModelScope)
    }

    private fun handleException(error: Throwable) {
        val errorState = ErrorState(
            title = getString(R.string.common__error),
            message = error.message ?: getString(R.string.common__error_something_went_wrong)
        )
        updateUiState {
            it.copy(
                errorState = errorState
            )
        }
    }

    fun resetErrorState() {
        updateUiState {
            it.copy(
                errorState = null
            )
        }
    }

    fun activityBack() {
        if (handleBack().not()) {
            viewModelScope.sendEvent(Event.Finish)
        }
    }
}
