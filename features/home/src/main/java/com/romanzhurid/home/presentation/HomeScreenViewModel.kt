package com.romanzhurid.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState.ErrorType
import com.romanzhurid.brandbook.ext.EMPTY_STRING
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.mapper.ExceptionMapper
import com.romanzhurid.common.permisison.PermissionHelper
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.bus.interactor.BusEndPointInteractor
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import com.romanzhurid.domain.currencies.interactor.WeatherInteractor
import com.romanzhurid.domain.exception.NoLocationPermissionException
import com.romanzhurid.domain.location.LocationRepository
import com.romanzhurid.home.mapper.BusStationsListUiMapper
import com.romanzhurid.home.mapper.WeatherUiMapper
import com.romanzhurid.home.model.StationUi
import com.romanzhurid.home.model.HomeBottomMenu
import com.romanzhurid.home.model.HomeBottomMenuType
import com.romanzhurid.home.model.WeatherState
import com.romanzhurid.home.presentation.HomeScreenViewModel.Event
import com.romanzhurid.home.presentation.HomeScreenViewModel.UiState
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.AppRoute.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext

class HomeScreenViewModel(
    progressDelegate: ProgressDelegate,
    private val weatherInteractor: WeatherInteractor,
    private val weatherUiMapper: WeatherUiMapper,
    private val exceptionMapper: ExceptionMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val permissionHelper: PermissionHelper,
    private val locationRepository: LocationRepository,
    private val currenciesInteractor: CurrenciesInteractor,
    private val busEndPointInteractor: BusEndPointInteractor,
    private val busStationsListUiMapper: BusStationsListUiMapper,
    private val res: ResourceProvider
) :
    ViewModel(),
    UiStateDelegate<UiState, Event> by UiStateDelegateImpl(UiState(
        bottomMenu = HomeBottomMenu.getDefaultMenu()
    )),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val isLoading: Boolean = false,
        val weather: WeatherState = WeatherState.Loading,
        val currency: String = EMPTY_STRING,
        val bottomMenu: List<HomeBottomMenu>,
        val stations: List<StationUi> = emptyList(),
    )

    sealed interface Event {
        data object RequestLocationPermission : Event
        data object ToGlobalSettings : Event
        data class NavigateTo(val route: AppRoute) : Event
    }

    private var navigationInProgress = false

    private val exceptionHandler = viewModelScope.exceptionHandler {

    }

    init {
        loadWeather()
        loadCurrencies()
        observeStations()
    }

    fun onNavigate(homeBottomMenuType: HomeBottomMenuType) {
        if (navigationInProgress) return

        navigationInProgress = true

        viewModelScope.launch {
            val route = when(homeBottomMenuType) {
                HomeBottomMenuType.Settings -> {
                    Settings()
                }
                HomeBottomMenuType.Cinema -> {
                    Cinema()
                }
                HomeBottomMenuType.Currencies -> {
                    Currencies()
                }
                HomeBottomMenuType.DeliveryFood -> {
                    DeliveryFood()
                }
                is HomeBottomMenuType.BusRoutes -> {
                    Bus(stationId = homeBottomMenuType.busNumber)
                }
            }
            sendEvent(Event.NavigateTo(route))
        }
    }

    fun onResume() {
        navigationInProgress = false

        when ((stateValue.weather as? WeatherState.Error)?.errorType) {
            ErrorType.LOCATION_UNAVAILABLE -> loadWeather()
            else -> Unit
        }
    }

    // region BUS STATIONS
    private fun observeStations() {
        busEndPointInteractor
            .observeAll()
            .onEach { stations ->
                val stations = stations.map(busStationsListUiMapper::map)
                updateUiState { it.copy(stations = stations) }
            }
            .catch { exception ->
                exceptionHandler.handleException(EmptyCoroutineContext, exception)
            }
            .launchIn(viewModelScope)
    }

    fun onReturnedFromSettings() {
        if (permissionHelper.isLocationPermissionGranted()) {
            loadWeather()
        }
    }

    fun onLocationPermissionResult(
        granted: Boolean,
        permanentlyDenied: Boolean
    ) {
        when {
            granted -> {
                loadWeather()
            }
            permanentlyDenied -> {
                updateUiState {
                    it.copy(
                        weather = WeatherState.Error(
                            message = res.getString(R.string.weather__exception_no_location_permission),
                            errorType = ErrorType.PERMISSION_PERMANENTLY_DENIED
                        )
                    )
                }
            }
            else -> {
                updateUiState {
                    it.copy(
                        weather = WeatherState.Error(
                            message = res.getString(R.string.weather__exception_no_location_permission),
                            errorType = ErrorType.LOCATION_NO_PERMISSION
                        )
                    )
                }
            }
        }
    }

    // region WEATHER
    fun loadWeather() {
        viewModelScope.launch(exceptionHandler) {
            updateUiState {
                it.copy(weather = WeatherState.Loading)
            }
            runCatching {
                val location = withContext(dispatcherProvider.background()) {
                    if (permissionHelper.isLocationPermissionGranted()) {
                        locationRepository.getLocation()
                    } else {
                        throw NoLocationPermissionException()
                    }
                }
                val weather = withContext(dispatcherProvider.background()) {
                    weatherInteractor.getWeather(
                        lat = location.latitude,
                        lon = location.longitude
                    )
                }
                weatherUiMapper.map(weather)
            }.onSuccess { weatherUi ->
                updateUiState {
                    it.copy(weather = WeatherState.Success(weatherUi))
                }
            }.onFailure { throwable ->
                val (message, error) = exceptionMapper.mapWeatherException(throwable)
                updateUiState {
                    it.copy(
                        weather = WeatherState.Error(
                            message = message,
                            errorType = error
                        )
                    )
                }
            }
        }
    }

    fun onWeatherErrorClicked(errorType: ErrorType) {
        when (errorType) {
            ErrorType.LOCATION_NO_PERMISSION -> {
                viewModelScope.sendEvent(Event.RequestLocationPermission)
            }
            ErrorType.PERMISSION_PERMANENTLY_DENIED -> {
                viewModelScope.sendEvent(Event.ToGlobalSettings)
            }
            else -> {
                loadWeather()
            }
        }
    }
    // endregion

    // region CURRENCIES
    private fun loadCurrencies() {
        viewModelScope.launch(CoroutineExceptionHandler { context, throwable ->
            updateUiState {
                it.copy(currency = res.getString(R.string.error__network_default_error))
            }
        }) {
            val currency = withContext(dispatcherProvider.background()) {
                currenciesInteractor.getCurrencyById()
            }
            updateUiState {
                it.copy(currency = "${currency.abbreviation} ${currency.officialRate}")
            }
        }
    }
    // endregion
}