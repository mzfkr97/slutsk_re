package com.romanzhurid.home.presentation

import android.Manifest
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import coil3.compose.AsyncImage
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState.ErrorType
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.ext.ResumeEffect
import com.romanzhurid.common.ext.appSettingsIntent
import com.romanzhurid.common.ext.launchLocationPermission
import com.romanzhurid.common.ext.singleClick
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.home.model.StationUi
import com.romanzhurid.home.model.HomeBottomMenu
import com.romanzhurid.home.model.HomeBottomMenuType
import com.romanzhurid.home.model.WeatherState
import com.romanzhurid.home.model.WeatherUi
import com.romanzhurid.home.presentation.HomeScreenViewModel.Event
import com.romanzhurid.home.presentation.HomeScreenViewModel.UiState
import com.romanzhurid.navigation.composition.LocalNavigator

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.collectUiState()
    val activity = LocalActivity.current

    val locationPermissionLauncher =
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
            is Event.RequestLocationPermission -> {
                locationPermissionLauncher.launchLocationPermission()
            }
            Event.ToGlobalSettings -> {
                activity
                    ?.appSettingsIntent()
                    ?.let(appSettingsLauncher::launch)
            }
            is Event.NavigateTo -> {
                navigator.navigate(event.route)
            }
        }
    }

    ResumeEffect(onResume = viewModel::onResume)

    Scaffold(
        topBar = {
            AppToolbar(
                title = R.string.home__title,
                actionIcon = Icons.Outlined.Settings,
                showBackBtn = false,
                onActionClick = {
                    viewModel.onNavigate(HomeBottomMenuType.SETTINGS)
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                HeaderCard(
                    uiState = uiState,
                    onWeatherErrorClicked = viewModel::onWeatherErrorClicked,
                    onCurrenciesClicked = {
                        viewModel.onNavigate(HomeBottomMenuType.CURRENCIES)
                    }
                )
            }

            item {
                StationCard(
                    stations = uiState.stations,
                    modifier = Modifier.fillMaxWidth(),
                    onAllRouteClicked = {
                        viewModel.onNavigate(HomeBottomMenuType.ROUTES)
                    }
                )
            }

            items(
                items = uiState.bottomMenu,
                key = { it.menuType }
            ) { menu ->
                BottomCard(
                    homeBottomMenu = menu,
                    onBottomMenuClicked = { item ->
                        viewModel.onNavigate(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun HeaderCard(
    uiState: UiState,
    onWeatherErrorClicked: (errorType: ErrorType) -> Unit,
    onCurrenciesClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .defaultMinSize(minHeight = 48.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WeatherCard(
            weatherState = uiState.weather,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            onWeatherErrorClicked = onWeatherErrorClicked
        )

        Currency(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            uiState.currency,
            onCurrenciesClicked
        )
    }
}

@Composable
private fun StationCard(
    stations: List<StationUi>,
    modifier: Modifier = Modifier,
    onAllRouteClicked: () -> Unit
) {
    AppCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsBus,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Автобусы",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                TextButton(
                    onClick = onAllRouteClicked
                ) {
                    Text("Все рейсы")
                }
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stations.forEach { station ->
                    AppCard (
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = station.busNumber.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

// region WEATHER
@Composable
private fun WeatherCard(
    modifier: Modifier = Modifier,
    weatherState: WeatherState,
    onWeatherErrorClicked: (errorType: ErrorType) -> Unit
) {
    Box(modifier = modifier) {
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
    Row {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
// endregion

// region CURRENCY
@Composable
private fun Currency(
    modifier: Modifier,
    currency: String,
    onCurrenciesClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = currency,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.clickable {
                onCurrenciesClicked.invoke()
            },
            text = "Больше курсов",
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colorScheme.primary
        )
    }
}
// endregion

@Composable
private fun BottomCard(
    homeBottomMenu: HomeBottomMenu,
    onBottomMenuClicked: (HomeBottomMenuType) -> Unit
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable(onClick = {
                onBottomMenuClicked(homeBottomMenu.menuType)
            })
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(homeBottomMenu.backgroundResId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Text(
                text = stringResource(homeBottomMenu.titleResId),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}
