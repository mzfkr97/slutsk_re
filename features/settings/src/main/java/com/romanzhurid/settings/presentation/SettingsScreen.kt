package com.romanzhurid.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.domain.ThemeMode

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(AppTheme.dimensions.medium),
        ) {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(AppTheme.dimensions.medium)) {
                    ThemeDropdown(
                        selected = uiState.themeMode,
                        onSelected = viewModel::onThemeChanged,
                        modifier = Modifier.padding(bottom = AppTheme.dimensions.medium)
                    )
                    CurrencyDropdown(
                        selectedId = uiState.currencyId,
                        onSelected = viewModel::onCurrencyChanged
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeDropdown(
    selected: ThemeMode,
    onSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = stringResource(selected.titleRes),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.settings__theme)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ThemeMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(stringResource(mode.titleRes)) },
                    onClick = {
                        onSelected(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}

private val ThemeMode.titleRes: Int
    get() = when (this) {
        ThemeMode.LIGHT -> R.string.settings__theme_light
        ThemeMode.DARK -> R.string.settings__theme_dark
        ThemeMode.SYSTEM -> R.string.settings__theme_system
    }

private enum class Currency(val titleRes: Int, val id: Int) {
    USD(R.string.settings__currency_usd, 431),
    EUR(R.string.settings__currency_eur, 451),
    PLN(R.string.settings__currency_pln, 452),
    RUB(R.string.settings__currency_rub, 456)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(selectedId: Int, onSelected: (Int) -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val selected = Currency.entries.firstOrNull { it.id == selectedId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = selected?.let { stringResource(it.titleRes) }
                ?: stringResource(R.string.settings__currency_unknown, selectedId),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(stringResource(R.string.settings__currency))
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Currency.entries.forEach { currency ->
                DropdownMenuItem(
                    text = {
                        Text(stringResource(currency.titleRes))
                    },
                    onClick = {
                        onSelected(currency.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
