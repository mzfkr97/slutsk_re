package com.romanzhurid.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.ext.DefaultSpacer
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.home.model.WeatherState
import com.romanzhurid.home.model.WeatherUi
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.collectUiState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onResume()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                .padding(4.dp)
        ) {

            HeaderCard(uiState = uiState)

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
private fun HeaderCard(uiState: HomeScreenViewModel.UiState) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(124.dp),
        borderColor = AppTheme.colorScheme.primary
    ) {
        WeatherCard(weatherState = uiState.weather)
    }
}

@Composable
private fun WeatherCard(weatherState: WeatherState) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()
    ) {
        when (weatherState) {
            is WeatherState.Error -> {
                Text(
                    text = weatherState.message,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            WeatherState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    strokeWidth = AppTheme.dimensions.xMicro,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            is WeatherState.Success -> {
                WeatherContent(ui = weatherState.weather)
            }
        }
    }
}

@Composable
fun WeatherContent(ui: WeatherUi) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ui.temperature,
                    style = MaterialTheme.typography.headlineSmall,
                    color = AppTheme.colorScheme.primary
                )

                AsyncImage(
                    model = ui.iconCode,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${ui.cityName}\n${ui.description}",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = ui.currentDate,
                style = MaterialTheme.typography.titleSmall,
                color = AppTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun StationsCard() {
    AppCard(
        borderColor = AppTheme.colorScheme.primary,
    ) {
        Button(onClick = {}) {
            Text(text = "StationsCard")
        }
    }
}

@Composable
private fun CinemaCard(onCinemaClicked: () -> Unit) {
    AppCard(
        borderColor = AppTheme.colorScheme.primary,
    ) {
        Column() {

        }
        Button(onClick = { onCinemaClicked() }) {
            Text(text = "Cinema")
        }
    }
}
