package com.romanzhurid.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ExceptionsEmitter
import com.romanzhurid.common.mapper.ExceptionMapper
import com.romanzhurid.common.permisison.PermissionHelper
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.location.LocationRepository
import com.romanzhurid.domain.currencies.interactor.WeatherInteractor
import com.romanzhurid.domain.exception.NoLocationPermissionException
import com.romanzhurid.home.mapper.WeatherUiMapper
import com.romanzhurid.home.model.WeatherState
import com.romanzhurid.home.presentation.HomeScreenViewModel.*
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
) :
    ViewModel(),
    UiStateDelegate<UiState, Event> by UiStateDelegateImpl(UiState()),
    ProgressDelegate by progressDelegate {

    data class UiState(
        val isLoading: Boolean = false,
        val weather: WeatherState = WeatherState.Loading,
    )

    sealed interface Event

    private val exceptionHandler = viewModelScope.exceptionHandler {

    }

    init {
        loadWeather()

    }

    fun onResume() {
        if (stateValue.weather is WeatherState.Success) return

        if ((stateValue.weather as? WeatherState.Error)?.isNoLocation == true) {
            loadWeather()
        }
    }

    private fun loadWeather() {
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
                val (message, isNoLocation) = exceptionMapper.mapWeatherException(throwable)
                updateUiState {
                    it.copy(
                        weather = WeatherState.Error(
                            message = message,
                            isNoLocation = isNoLocation
                        )
                    )
                }
            }
        }
    }
}