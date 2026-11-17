package com.romanzhurid.home.presentation

import android.Manifest
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import coil3.compose.AsyncImage
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.card.AppClickableCard
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState.ErrorType
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.ext.ResumeEffect
import com.romanzhurid.common.ext.appSettingsIntent
import com.romanzhurid.common.ext.launchLocationPermission
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.home.model.HomeBottomMenu
import com.romanzhurid.home.model.HomeBottomMenuType
import com.romanzhurid.home.model.StationUi
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AppToolbar(
                    title = R.string.home__title,
                    subtitle = stringResource(R.string.home__subtitle),
                    actionIcon = Icons.Outlined.Settings,
                    actionContentDescription = stringResource(R.string.settings__screen_title),
                    showBackBtn = false,
                    onActionClick = {
                        viewModel.onNavigate(HomeBottomMenuType.Settings)
                    },
                )
            }

        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                top = AppTheme.dimensions.small,
                start = AppTheme.dimensions.medium,
                end = AppTheme.dimensions.medium,
                bottom = AppTheme.dimensions.xxxMedium
            ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.medium)
        ) {

            item {
                HeaderCard(
                    uiState = uiState,
                    onWeatherErrorClicked = viewModel::onWeatherErrorClicked,
                    onCurrenciesClicked = {
                        viewModel.onNavigate(HomeBottomMenuType.Currencies)
                    }
                )
            }
            item {
                StationCard(
                    stations = uiState.stations,
                    modifier = Modifier.fillMaxWidth(),
                    onRouteClicked = { stationId ->
                        viewModel.onNavigate(HomeBottomMenuType.BusRoutes(stationId))
                    }
                )
            }
            items(
                items = uiState.bottomMenu,
                key = { it.menuType.hashCode() }
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
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimensions.medium)
                .height(IntrinsicSize.Min)
                .defaultMinSize(minHeight = AppTheme.dimensions.buttonHeight),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.medium)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
            ) {
                Text(
                    text = stringResource(R.string.home__weather),
                    style = AppTheme.typography.labelMedium,
                    color = AppTheme.colorScheme.onSurfaceVariant
                )
                WeatherCard(
                    weatherState = uiState.weather,
                    onWeatherErrorClicked = onWeatherErrorClicked
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(AppTheme.colorScheme.outlineVariant)
            )
            Currency(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                currency = uiState.currency,
                onCurrenciesClicked = onCurrenciesClicked
            )
        }
    }
}

@Composable
private fun StationCard(
    stations: List<StationUi>,
    modifier: Modifier = Modifier,
    onRouteClicked: (Int?) -> Unit
) {
    AppCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(AppTheme.dimensions.medium)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
            ) {
                val favoriteStations = stations.filter { it.isFavorite }
                val otherStations = stations.filterNot { it.isFavorite }
                if (favoriteStations.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xxMicro)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(AppTheme.dimensions.xxMedium),
                            tint = AppTheme.colorScheme.tertiary
                        )

                        Text(
                            text = stringResource(R.string.common__favorite_routes),
                            style = AppTheme.typography.titleMedium,
                        )
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
                    ) {
                        favoriteStations.forEach { station ->
                            BusChipCompact(
                                number = station.busNumber.toString(),
                                onClick = {
                                    onRouteClicked(station.busNumber)
                                }
                            )
                        }
                    }
                }

                if (otherStations.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.dimensions.small),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DirectionsBus,
                            contentDescription = null,
                            modifier = Modifier.size(AppTheme.dimensions.xxMedium),
                            tint = AppTheme.colorScheme.primary
                        )

                        Text(
                            text = stringResource(R.string.common__all_routes),
                            style = AppTheme.typography.titleMedium,
                        )
                    }

                    val itemsPerPage = 3
                    val pages = otherStations.chunked(itemsPerPage)
                    val pagerState = rememberPagerState { pages.size }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(
                            end = if (pages.size > 1) AppTheme.dimensions.large else 0.dp
                        ),
                        pageSpacing = AppTheme.dimensions.small
                    ) { pageIndex ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
                        ) {
                            pages[pageIndex].forEach { station ->
                                BusChipDetails(
                                    number = station.busNumber.toString(),
                                    routeName = station.routeName,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        onRouteClicked(station.busNumber)
                                    }
                                )
                            }
                            if (pages.size > 1) {
                                repeat(itemsPerPage - pages[pageIndex].size) {
                                    AppCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .alpha(0f)
                                            .clearAndSetSemantics {}
                                    ) {
                                        BusChipDetailsContent(
                                            number = pages[pageIndex].first().busNumber.toString(),
                                            routeName = pages[pageIndex].first().routeName
                                        )
                                    }
                                }
                            }
                        }
                    }

                    RoutePagerIndicator(
                        pageCount = pages.size,
                        currentPage = pagerState.currentPage
                    )
                }
            }

            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    onRouteClicked(null)
                },
                shape = RoundedCornerShape(AppTheme.dimensions.xxSmall),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = AppTheme.colorScheme.primaryContainer,
                    contentColor = AppTheme.colorScheme.onPrimaryContainer
                ),
                contentPadding = PaddingValues(
                    horizontal = AppTheme.dimensions.medium,
                    vertical = AppTheme.dimensions.xxMicro
                )
            ) {
                Text(
                    text = stringResource(R.string.common__show_all),
                    style = AppTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.width(AppTheme.dimensions.small))
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.dimensions.xMedium)
                )
            }
        }
    }
}

@Composable
private fun RoutePagerIndicator(pageCount: Int, currentPage: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimensions.small),
        horizontalArrangement = Arrangement.spacedBy(
            AppTheme.dimensions.small,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val width by animateDpAsState(
                targetValue = if (isSelected) AppTheme.dimensions.xxMedium else AppTheme.dimensions.small,
                label = "routeIndicatorWidth"
            )
            val color by animateColorAsState(
                targetValue = if (isSelected) {
                    AppTheme.colorScheme.primary
                } else {
                    AppTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                },
                label = "routeIndicatorColor"
            )
            Box(
                modifier = Modifier
                    .width(width)
                    .height(AppTheme.dimensions.small)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}

@Composable
private fun BusChipCompact(
    number: String,
    onClick: () -> Unit
) {
    AppClickableCard(
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = AppTheme.dimensions.buttonHeightLarge)
                .height(AppTheme.dimensions.buttonHeight)
                .padding(horizontal = AppTheme.dimensions.small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = AppTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BusChipDetails(
    number: String,
    routeName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AppClickableCard(
        onClick = onClick,
        modifier = modifier
    ) {
        BusChipDetailsContent(number = number, routeName = routeName)
    }
}

@Composable
private fun BusChipDetailsContent(number: String, routeName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.dimensions.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xxSmall)
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(
                    minWidth = AppTheme.dimensions.buttonHeightSmall,
                    minHeight = AppTheme.dimensions.buttonHeightSmall
                )
                .background(
                    color = AppTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(AppTheme.dimensions.xxSmall)
                )
                .padding(horizontal = AppTheme.dimensions.small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = AppTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                style = AppTheme.typography.titleMedium
            )
        }
        Text(
            text = routeName,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
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
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = ui.temperature,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.onSurface
            )
            AsyncImage(
                model = ui.iconCode,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
        }

        Text(
            text = "${ui.cityName}\n${ui.description}",
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colorScheme.onSurfaceVariant
        )
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
        modifier = modifier.clickable(onClick = onCurrenciesClicked),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
    ) {
        Text(
            text = stringResource(R.string.home__currency),
            style = AppTheme.typography.labelMedium,
            color = AppTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = currency,
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.common__more_courses),
            style = MaterialTheme.typography.labelMedium,
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
    AppClickableCard(
        onClick = { onBottomMenuClicked(homeBottomMenu.menuType) },
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
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
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.08f),
                                Color.Black.copy(alpha = 0.75f)
                            )
                        )
                    )
            )

            Text(
                text = stringResource(homeBottomMenu.titleResId),
                color = Color.White,
                style = AppTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(AppTheme.dimensions.medium)
            )
        }
    }
}
