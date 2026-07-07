package com.romanzhurid.onboarding.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.AppSettings
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.onboarding.model.IntroPage
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel.Event
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel.UiState
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val appSettings: AppSettings,
    progressDelegate: ProgressDelegate,
) : ViewModel(),
    UiStateDelegate<UiState, Event> by UiStateDelegateImpl(UiState()),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val pages: List<IntroPage> = IntroPage.getPages(),
        val isNextActionEnabled: Boolean = false
    )

    sealed interface Event {
        data class OnClearAndPush(val destination: AppRoute) : Event
    }

    fun onFinishIntro() {
        viewModelScope.launch {
            appSettings.isFirstAppStart = false
            sendEvent(Event.OnClearAndPush(AppRoute.Home()))
        }
    }
}
