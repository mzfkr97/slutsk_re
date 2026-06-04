package com.romanzhurid.onboarding.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.R
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.AppSettings
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel.Event
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel.UiState
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.onboarding.navigation.OnboardingFeatureRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val appSettings: AppSettings,
    progressDelegate: ProgressDelegate,
) : ViewModel(),
    UiStateDelegate<UiState, Event> by UiStateDelegateImpl(UiState()),
    ProgressDelegate by progressDelegate {

    private val exceptionHandler = viewModelScope.exceptionHandler() {
        updateUiState { state ->
            state.copy(
                isNextActionEnabled = false,
            )
        }
    }

    data class UiState(
        val isNextActionEnabled: Boolean = false
    )

    sealed interface Event {
        data class OnNavigate(val destination: OnboardingFeatureRoute) : Event
        data class OnOpenFeature(val destination: AppRoute) : Event
    }

    //TODO Test impl
    fun isNextActionChangedClicked() {
        updateUiState { state ->
            state.copy(
                isNextActionEnabled = stateValue.isNextActionEnabled.not(),
            )
        }
    }

    fun onSignInClick() {
        viewModelScope.launch(exceptionHandler) {
            showProgress(R.string.common__continue)

            delay(1000)

            throw IllegalStateException("Exception onSignInClick")
        }
    }
}
