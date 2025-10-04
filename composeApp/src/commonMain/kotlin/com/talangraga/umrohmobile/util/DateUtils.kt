@file:Suppress("unused")
package com.talangraga.umrohmobile.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

val INDONESIA_FULL: MonthNames = MonthNames(
    listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )
)

@OptIn(ExperimentalTime::class)
fun String.formatIsoTimestampToCustom(): String {
    return try {
        val instant = Instant.parse(this)
        // The 'Z' in the timestamp means UTC. We'll format it based on UTC.
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        val customFormat = LocalDateTime.Format {
            day(padding = Padding.NONE)
            // Day without leading zero
            char(' ')
            monthName(INDONESIA_FULL) // Full month name (e.g., "September")
            char(' ')
            year()
            char(',')
            char(' ')
            hour(Padding.ZERO) // Hour with leading zero (e.g., "04")
            char(':')
            minute(Padding.ZERO) // Minute with leading zero (e.g., "00")
        }
        localDateTime.format(customFormat)
    } catch (e: IllegalArgumentException) {
        // Handle cases where the string might not be a valid ISO timestamp
        println("Error parsing date: $this, ${e.message}")
        "Invalid Date" // Or return the original string, or throw
    }
}

fun formatDateRange(startDateString: String, endDateString: String): String {
    val startDate = LocalDate.parse(startDateString)
    val endDate = LocalDate.parse(endDateString)

    val dayMonthFormat = LocalDate.Format {
        day(padding = Padding.NONE)
        char(' ')
        monthName(INDONESIA_FULL)
    }

    val dayMonthYearFormat = LocalDate.Format {
        day(padding = Padding.NONE)
        char(' ')
        monthName(INDONESIA_FULL)
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