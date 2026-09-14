package com.romanzhurid.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.R
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
import com.romanzhurid.domain.currencies.model.Currency
import com.romanzhurid.domain.exception.LocationUnavailableException
import com.romanzhurid.domain.exception.NoLocationPermissionException
import com.romanzhurid.domain.location.LocationRepository
import com.romanzhurid.home.mapper.WeatherUiMapper
import com.romanzhurid.home.model.WeatherState
import com.romanzhurid.home.model.WeatherState.Error.ErrorType
import com.romanzhurid.home.presentation.HomeScreenViewModel.Event
import com.romanzhurid.home.presentation.HomeScreenViewModel.UiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
    UiStateDelegate<UiState, Event> by UiStateDelegateImpl(UiState()),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val isLoading: Boolean = false,
        val weather: WeatherState = WeatherState.Loading,
        val currency: String = EMPTY_STRING,
    )

    sealed interface Event {
        data object RequestLocationPermission : Event
        data object OpenAppSettings : Event
    }

    private val exceptionHandler = viewModelScope.exceptionHandler {

    }

    init {
        loadWeather()
        loadCurrencies()
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
                            errorType = ErrorType.NO_LOCATION_PERMISSION
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
                val weatherError = mapWeatherException(throwable)
                updateUiState {
                    it.copy(
                        weather = WeatherState.Error(
                            message = weatherError.message,
                            errorType = weatherError.errorType
                        )
                    )
                }
            }
        }
    }

    fun onWeatherErrorClicked(errorType: ErrorType) {
        when (errorType) {
            ErrorType.NO_LOCATION_PERMISSION -> {
                viewModelScope.sendEvent(Event.RequestLocationPermission)
            }
            ErrorType.PERMISSION_PERMANENTLY_DENIED -> {
                viewModelScope.sendEvent(Event.OpenAppSettings)
            }
            else -> {
                loadWeather()
            }
        }
    }

    private fun mapWeatherException(error: Throwable): WeatherState.Error {
        val (resId, errorType) = when (error) {
            is LocationUnavailableException -> {
                R.string.weather__exception_location_unavailable to ErrorType.LOCATION_UNAVAILABLE
            }
            is NoLocationPermissionException -> {
                R.string.weather__exception_no_location_permission to ErrorType.NO_LOCATION_PERMISSION
            }

            is UnknownHostException -> {
                R.string.weather__exception_no_internet to ErrorType.UNKNOWN
            }

            is SocketTimeoutException -> {
                R.string.weather__exception_timeout to ErrorType.UNKNOWN
            }

            else -> {
                R.string.weather__exception_default to ErrorType.UNKNOWN
            }
        }
        return WeatherState.Error(res.getString(resId), errorType)
    }
    // endregion

    // region currencies

    private fun loadCurrencies() {
        viewModelScope.launch {
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