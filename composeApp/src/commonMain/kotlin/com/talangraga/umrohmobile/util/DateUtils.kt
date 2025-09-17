@file:Suppress("unused")
package com.talangraga.umrohmobile.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun formatDateRange(startDateString: String, endDateString: String): String {
    val startDate = LocalDate.parse(startDateString)
    val endDate = LocalDate.parse(endDateString)

    val dayMonthFormat = LocalDate.Format {
        day(padding = Padding.NONE)
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
    }

    val dayMonthYearFormat = LocalDate.Format {
        day(padding = Padding.NONE)
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        year()
    }

//    val monthYearFormat = LocalDate.Format {
//        monthName(MonthNames.ENGLISH_FULL)
//        char(' ')
//        year()
//    }

    return try {
        when {
            startDate.year != endDate.year -> {
                "${startDate.format(dayMonthYearFormat)} - ${endDate.format(dayMonthYearFormat)}"
            }

            startDate.month.number != endDate.month.number -> {
                "${startDate.format(dayMonthFormat)} - ${endDate.format(dayMonthYearFormat)}"
            }

            else -> { // Same month and year
                "${startDate.day} - ${endDate.format(dayMonthYearFormat)}" // Used dayOfMonth directly here
            }
        }
    } catch (ex: Exception) {
        ""
    }
}


fun LocalDate.isDateInRange(
    startDateString: String,
    endDateString: String
): Boolean {
    return try {
        val startDate = LocalDate.parse(startDateString)
        val endDate = LocalDate.parse(endDateString)
        this >= startDate && this <= endDate
    } catch (e: Exception) {
        println("Error parsing dates: ${e.message}")
        false
    }
}

@OptIn(ExperimentalTime::class)
val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

val dayOfMonth = currentDate.day            // Integer, e.g., 23
val month = currentDate.month               // Month enum, e.g., Month.JULY
val monthName = currentDate.month.name      // String, e.g., "JULY" (you can convert to lowercase or title case if needed)
val monthNumber = currentDate.month.number  // Integer, e.g., 7
val year = currentDate.year                 // Integer, e.g., 2025