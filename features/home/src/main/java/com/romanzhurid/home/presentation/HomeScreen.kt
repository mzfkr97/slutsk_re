package com.romanzhurid.home.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.ext.DefaultSpacer
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.home.model.WeatherState
import com.romanzhurid.home.model.WeatherState.Error.ErrorType
import com.romanzhurid.home.model.WeatherUi
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.collectUiState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val activity = LocalActivity.current
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.values.any { it }
            val permanentlyDenied = granted.not() &&
                        activity != null &&
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ).not()

            viewModel.onLocationPermissionResult(
                granted = granted,
                permanentlyDenied = permanentlyDenied
            )
        }

    val appSettingsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.onReturnedFromSettings()
        }

    viewModel.CollectEventEffect { event ->
        when (event) {
            is HomeScreenViewModel.Event.RequestLocationPermission -> {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            HomeScreenViewModel.Event.OpenAppSettings -> {
                appSettingsLauncher.launch(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", activity?.packageName, null)
                    )
                )
            }
        }
    }

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

            HeaderCard(
                uiState = uiState,
                onWeatherErrorClicked = viewModel::onWeatherErrorClicked
            )

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
private fun HeaderCard(
    uiState: HomeScreenViewModel.UiState,
    onWeatherErrorClicked: (errorType: ErrorType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WeatherCard(
            weatherState = uiState.weather,
            modifier = Modifier.weight(1f),
            onWeatherErrorClicked = { errorType ->
                onWeatherErrorClicked(errorType)
            }
        )
    }
}

@Composable
private fun WeatherCard(
    modifier: Modifier = Modifier,
    weatherState: WeatherState,
    onWeatherErrorClicked: (errorType: ErrorType) -> Unit
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxHeight()
    ) {
        when (weatherState) {
            is WeatherState.Error -> {
                WeatherError(weatherState) {
                    onWeatherErrorClicked(weatherState.errorType)
                }
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
fun WeatherError(weatherError: WeatherState.Error, onWeatherErrorClicked: () -> Unit) {
    Text(
        text = weatherError.message,
        modifier = Modifier.clickable { onWeatherErrorClicked() },
        style = MaterialTheme.typography.bodySmall,
        color = AppTheme.colorScheme.error,
    )
}

@Composable
fun WeatherContent(ui: WeatherUi) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ui.temperature,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colorScheme.primary
                )
                AsyncImage(
                    model = ui.iconCode,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                )
            }

            Text(
                text = "${ui.cityName}\n${ui.description}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = ui.currentDate,
                style = MaterialTheme.typography.bodySmall,
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
