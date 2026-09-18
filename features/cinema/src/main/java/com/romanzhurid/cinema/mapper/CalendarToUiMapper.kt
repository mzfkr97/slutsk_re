package com.romanzhurid.cinema.mapper

import com.romanzhurid.brandbook.ext.getDay3LettersName
import com.romanzhurid.brandbook.ext.getDayNumber
import com.romanzhurid.brandbook.ext.thisDayIsMonday
import com.romanzhurid.cinema.model.CalendarUi
import com.romanzhurid.domain.cinema.Calendar

class CalendarToUiMapper {

    fun map(model: Calendar) =
        with(model) {
            CalendarUi(
                id = id,
                date = date,
                isItemSelected = isItemSelected,
                isMonday = date.thisDayIsMonday(),
                isClickable = true,
                calendarDay = date.getDay3LettersName(),
                calendarNumber = date.getDayNumber()
            )
        }
}
