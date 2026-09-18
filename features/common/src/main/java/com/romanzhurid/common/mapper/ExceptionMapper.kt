package com.romanzhurid.common.mapper

import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.domain.exception.LocationUnavailableException
import com.romanzhurid.domain.exception.NoLocationPermissionException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionMapper(resourceProvider: ResourceProvider) : ResourceProvider by resourceProvider {

    fun map(error: Throwable): ErrorState {
        return ErrorState(
            title = getString(R.string.common__error),
            message = error.message ?: getString(R.string.common__error_something_went_wrong)
        )
    }

    fun mapWeatherException(error: Throwable): Pair<String, ErrorState.ErrorType> {
        val (resId, errorType) = when (error) {
            is LocationUnavailableException -> {
                R.string.weather__exception_location_unavailable to ErrorState.ErrorType.LOCATION_UNAVAILABLE
            }
            is NoLocationPermissionException -> {
                R.string.weather__exception_no_location_permission to ErrorState.ErrorType.LOCATION_NO_PERMISSION
            }

            is UnknownHostException -> {
                R.string.weather__exception_no_internet to ErrorState.ErrorType.UNKNOWN
            }

            is SocketTimeoutException -> {
                R.string.weather__exception_timeout to ErrorState.ErrorType.UNKNOWN
            }

            else -> {
                R.string.weather__exception_default to ErrorState.ErrorType.UNKNOWN
            }
        }
        return getString(resId) to errorType
    }

}