package com.romanzhurid.brandbook.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateFormats {
    const val HH_MM_SS = "HH:mm:ss"
    const val HH_MM = "HH:mm"
}

@SuppressLint("ConstantLocale")
private val SHORT_TIME_FORMATTER = SimpleDateFormat(DateFormats.HH_MM, Locale.ROOT)

@SuppressLint("SimpleDateFormat")
fun String.currentTime(): String {
    val parser = SimpleDateFormat(DateFormats.HH_MM_SS)
    val formatter = SimpleDateFormat(DateFormats.HH_MM)
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return formatter.format(parser.parse(this))
}

fun Double.getTimeToDuration(): String {
    val input = this.toLong()
    val hours = (input - input % 3600) / 3600
    val minutes = (input % 3600 - input % 3600 % 60) / 60
    return "$hours ч. $minutes мин"
}

// get timestamp and return time root format
fun Long.getDateTimeFromEpocLongOfSeconds(): String {
    return try {
        val netDate = Date(this * 1000)
        return SHORT_TIME_FORMATTER.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

fun Date.thisDayIsMonday(locale: Locale = Locale.getDefault()): Boolean =
    SimpleDateFormat("EEEE", locale).format(this).equals("Monday", true) ||
        SimpleDateFormat("EEEE", locale).format(this).equals("Понедельник", true)

fun Date.getDay3LettersName(locale: Locale = Locale.getDefault()): String =
    SimpleDateFormat("EE", locale).format(this)

fun Date.getDayNumber(): String = SimpleDateFormat("dd", Locale.getDefault()).format(this)

private fun getFutureDates(count: Int): List<Date> {
    val futureDates = mutableListOf<Date>()
    val calendar = Calendar.getInstance(Locale.getDefault())
    for (i in 0 until count) {
        calendar.add(Calendar.DATE, 1)
        futureDates.add(calendar.time)
    }
    return futureDates
}

private fun getPastDates(count: Int): List<Date> {
    val pastDates = mutableListOf<Date>()
    val calendar = Calendar.getInstance(Locale.getDefault())
    for (i in 1..count) {
        calendar.add(Calendar.DATE, -1)
        pastDates.add(calendar.time)
    }
    return pastDates
}

fun getDates(
    pastDays: Int = 0,
    futureDays: Int = 6,
    includeCurrentDate: Boolean = true
): List<Date> {
    val futureList = getFutureDates(futureDays)
    val cal = Calendar.getInstance(Locale.US)
    val pastList = getPastDates(pastDays).reversed()
    val dateList = if (includeCurrentDate) {
        pastList + cal.time + futureList
    } else {
        pastList + futureList
    }
    return dateList
}
