package com.romanzhurid.home.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.romanzhurid.brandbook.components.button.AppBaseButton
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.home.navigation.HomeFeatureRoute
import com.romanzhurid.home.home.HomeViewModel.Event
import com.romanzhurid.home.home.HomeViewModel.UiState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    openAppRoute: (AppRoute) -> Unit,
    openFeatureRoute: (HomeFeatureRoute) -> Unit,
    onBack: () -> Unit,
) {

    val uiState by viewModel.collectUiState()

    viewModel.CollectEventEffect { event ->
        when (event) {
            is Event.OnNavigate -> {
                openFeatureRoute(event.destination)
            }
            is Event.OnOpenFeature -> {
                openAppRoute(event.destination)
            }
        }
    }

    HomeScreenContent(
        uiState = uiState,
        onNextActionChangedClicked = viewModel::isNextActionChangedClicked,
        onClick = viewModel::onSignInClick,
        onBack = onBack,
    )
}

@Composable
private fun HomeScreenContent(
    uiState: UiState,
    onNextActionChangedClicked: () -> Unit,
    onClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Home",
                onBack = onBack,
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimensions.medium),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
            ) {
                AppBaseButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Change state",
                    onClick = onNextActionChangedClicked
                )
                AppBaseButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "CHECK EXCEPTION",
                    isEnabled = uiState.isNextActionEnabled,
                    onClick = onClick
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppTheme.dimensions.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.medium),
        ) {
            Text("HomeScreen")
        }
    }
}