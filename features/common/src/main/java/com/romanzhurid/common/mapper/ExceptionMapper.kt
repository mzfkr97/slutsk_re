package com.romanzhurid.common.mapper

import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState
import com.romanzhurid.common.ResourceProvider

class ExceptionMapper(resourceProvider: ResourceProvider) : ResourceProvider by resourceProvider {

    fun map(error: Throwable): ErrorState {
        return ErrorState(
            title = getString(R.string.common__error),
            message = error.message ?: getString(R.string.common__error_something_went_wrong)
        )
    }
}