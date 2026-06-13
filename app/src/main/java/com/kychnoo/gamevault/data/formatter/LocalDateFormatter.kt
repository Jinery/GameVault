package com.kychnoo.gamevault.data.formatter

import kotlinx.datetime.LocalDate

fun LocalDate.format(): String {
    val month = month.name.take(3).lowercase().replaceFirstChar { it.uppercase() } // Take the first 3 letters of the month name and replace the first char to uppercase.
    return "$month ${day.toString().padStart(2, '0')}, $year" // Format to Mon, dd, yyyy.
}