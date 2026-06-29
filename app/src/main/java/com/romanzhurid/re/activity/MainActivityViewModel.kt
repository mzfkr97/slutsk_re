package com.romanzhurid.re.activity

import android.util.Log
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
import com.romanzhurid.navigation.navigator.AppNavigator
import com.romanzhurid.navigation.navigator.AppNavigatorImpl
import com.romanzhurid.navigation.navigator.NavigationStore
import com.romanzhurid.re.activity.MainActivityViewModel.Event
import com.romanzhurid.re.activity.MainActivityViewModel.UiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val appSettings: AppSettings,
    private val progressFlow: ProgressFlow,
    private val exceptionsFlow: ExceptionsFlow,
    res: ResourceProvider,
) : ViewModel(),
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

    private val navigationStore = NavigationStore<AppRoute>(
        initialStack = emptyList()
    )

    private val appNavigator: AppNavigator = AppNavigatorImpl(navigationStore)

    val backStack: StateFlow<List<AppRoute>> = navigationStore.backStack

    fun getNavigator(): AppNavigator = appNavigator

    init {
        initNavigation()
        observeNavigation()
        observeProgress()
        observeExceptions()
    }

    private fun initNavigation() {
        viewModelScope.launch {
            val startStack = resolveBackStack()

            if (startStack.isNotEmpty()) {
                appNavigator.clearAndPush(startStack.first())
                startStack.drop(1).forEach(appNavigator::navigate)
            }
        }
    }

    private fun resolveBackStack(): List<AppRoute> {
        return if (appSettings.isFirstAppStart) {
            listOf(AppRoute.Onboarding())
        } else {
            listOf(AppRoute.Home())
        }
    }

    private fun observeNavigation() {
        backStack
            .onEach { stack ->
                updateUiState { it.copy(backStack = stack) }
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
        Log.d("TAG", "handleException: $errorState")
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
        if (appNavigator.back().not()) {
            viewModelScope.sendEvent(Event.Finish)
        }
    }
}
