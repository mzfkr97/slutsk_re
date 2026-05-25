package com.romanzhurid.brandbook.components.errorbottomsheet

import com.romanzhurid.brandbook.ext.EMPTY_STRING

data class ErrorState(
    val title: String? = null,
    val message: String = EMPTY_STRING,
    val type: ErrorType = ErrorType.UNKNOWN
) {
    enum class ErrorType {
        UNKNOWN,
        NO_INTERNET,
        NO_CONNECTION_TO_SERVER
    }
}