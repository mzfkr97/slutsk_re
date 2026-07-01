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
import com.romanzhurid.re.activity.MainActivityViewModel.Event
import com.romanzhurid.re.activity.MainActivityViewModel.UiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        val errorState: ErrorState? = null,
        val isDarkTheme: Boolean = false
    )

    sealed interface Event {
        data object Finish : Event
    }

    init {
        observeProgress()
        observeExceptions()
        observeTheme()
    }

    private fun observeTheme() {
        appSettings.isDarkThemeFlow
            .onEach { isDark ->
                updateUiState { it.copy(isDarkTheme = isDark) }
            }
            .launchIn(viewModelScope)
    }

    fun finish() {
        viewModelScope.sendEvent(Event.Finish)
    }

    fun resolveBackStack(): List<AppRoute> {
        return if (appSettings.isFirstAppStart) {
            listOf(AppRoute.Onboarding())
        } else {
            listOf(AppRoute.Home())
        }
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
}
