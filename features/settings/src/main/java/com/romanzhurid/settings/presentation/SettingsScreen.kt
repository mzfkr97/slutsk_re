package com.romanzhurid.settings.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.checkbox.AppCheckBox
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.collectUiState

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.collectUiState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = R.string.settings__screen_title,
                onBack = onBackClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(AppTheme.dimensions.small),
            contentAlignment = Alignment.TopCenter
        ) {
            AppCheckBox(
                title = R.string.settings__dark_theme,
                checked = uiState.isDarkTheme,
                onCheckedChange = viewModel::onThemeChanged
            )
        }
    }
}
