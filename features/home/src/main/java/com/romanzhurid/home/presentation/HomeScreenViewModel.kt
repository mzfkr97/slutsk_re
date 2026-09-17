package com.romanzhurid.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState.ErrorType
import com.romanzhurid.brandbook.ext.EMPTY_STRING
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ExceptionsEmitter
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.mapper.ExceptionMapper
import com.romanzhurid.common.permisison.PermissionHelper
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.currencies.interactor.CurrenciesInteractor
import com.romanzhurid.domain.currencies.interactor.WeatherInteractor
import com.romanzhurid.domain.exception.NoLocationPermissionException
import com.romanzhurid.domain.location.LocationRepository
import com.romanzhurid.home.mapper.WeatherUiMapper
import com.romanzhurid.home.model.HomeBottomMenu
import com.romanzhurid.home.model.HomeBottomMenuType
import com.romanzhurid.home.model.WeatherState
import com.romanzhurid.home.presentation.HomeScreenViewModel.Event
import com.romanzhurid.home.presentation.HomeScreenViewModel.UiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(
    progressDelegate: ProgressDelegate,
    private val weatherInteractor: WeatherInteractor,
    private val weatherUiMapper: WeatherUiMapper,
    private val exceptionsEmitter: ExceptionsEmitter,
    private val exceptionMapper: ExceptionMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val permissionHelper: PermissionHelper,
    private val locationRepository: LocationRepository,
    private val currenciesInteractor: CurrenciesInteractor,
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
        val bottomMenu: List<HomeBottomMenu>
    )

    sealed interface Event {
        data object RequestLocationPermission : Event
        data object ToAppSettings : Event
        data object ToGlobalSettings : Event
        data object ToCinema : Event
        data object ToCurrencies : Event
        data object ToDeliveryFood : Event
    }

    private val exceptionHandler = viewModelScope.exceptionHandler {

    }

    init {
        loadWeather()
        loadCurrencies()
    }

    fun onNavigate(homeBottomMenuType: HomeBottomMenuType) {
        viewModelScope.launch {
            val event = when(homeBottomMenuType) {
                HomeBottomMenuType.SETTINGS -> {
                    Event.ToAppSettings
                }
                HomeBottomMenuType.CINEMA -> {
                    Event.ToCinema
                }
                HomeBottomMenuType.CURRENCIES -> {
                    Event.ToCurrencies
                }
                HomeBottomMenuType.DELIVERY_FOOD -> {
                    Event.ToDeliveryFood
                }
            }
            sendEvent(event)
        }
    }

    fun onResume() {
        when ((stateValue.weather as? WeatherState.Error)?.errorType) {
            ErrorType.LOCATION_UNAVAILABLE -> loadWeather()
            else -> Unit
        }
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

    // region weather
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

    // region currencies
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