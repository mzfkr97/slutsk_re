package com.romanzhurid.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.ext.DefaultSpacer
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator

@Composable
fun HomeScreen() {
    val navigator = LocalNavigator.current
    Scaffold(
        topBar = {
            AppToolbar(
                title = R.string.home__title,
                actionIcon = Icons.Outlined.Settings,
                showBackBtn = false,
                onActionClick = {
                    navigator.navigate(AppRoute.Settings())
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            HeaderCard {
                navigator.navigate(AppRoute.Currencies())
            }

            DefaultSpacer()

            StationsCard()

            DefaultSpacer()

            CinemaCard {
                navigator.navigate(AppRoute.Cinema())
            }
        }
    }
}

@Composable
private fun HeaderCard(onCurrencyClicked : () -> Unit ) {
    AppCard(borderColor = AppTheme.colorScheme.primary) {
        Button(onClick = { onCurrencyClicked() }) {
            Text(text = "Go to Currencies")
        }
    }
}

@Composable
private fun StationsCard() {
    AppCard(
        borderColor = AppTheme.colorScheme.primary,
        modifier = Modifier
            .padding(AppTheme.dimensions.medium)
    ) {
        Column() {

        }
        Button(onClick = {}) {
            Text(text = "StationsCard")
        }
    }
}


@Composable
private fun CinemaCard(onCinemaClicked: () -> Unit) {
    AppCard(
        borderColor = AppTheme.colorScheme.primary,
        modifier = Modifier
            .padding(AppTheme.dimensions.medium)
    ) {
        Column() {

        }
        Button(onClick = { onCinemaClicked() }) {
            Text(text = "Cinema")
        }
    }
}
