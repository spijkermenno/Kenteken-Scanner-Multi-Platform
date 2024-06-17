package ui.extensions

import kotlinx.datetime.LocalDate

fun String.formatDateFromString(): String {
    val dateString = this

    if (dateString.length < 8) {
        // Date string could not be parsed, return the original string
        return dateString
    }

    // Parse the input date string to LocalDate
    val year = dateString.substring(0, 4).toInt()
    val month = dateString.substring(4, 6).toInt()
    val day = dateString.substring(6, 8).toInt()
    val localDate = LocalDate(year, month, day)

    // Format the date to the desired output string
    val monthName = localDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "${localDate.dayOfMonth} $monthName ${localDate.year}"
}
