package com.romanzhurid.cinema.model

import java.util.Date

data class CalendarUi(
    val id: Int,
    val date: Date,
    val isItemSelected: Boolean,
    val isMonday: Boolean,
    val isClickable: Boolean,
    val calendarNumber: String,
    val calendarDay: String
)
