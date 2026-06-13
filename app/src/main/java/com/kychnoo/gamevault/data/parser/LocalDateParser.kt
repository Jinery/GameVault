package com.kychnoo.gamevault.data.parser

import kotlinx.datetime.LocalDate

fun String?.toLocalDateOrNull(): LocalDate? {
    return this?.let {
        try {
            LocalDate.parse(it) // Try parse to local date.
        } catch (_: Exception) {
            null // Return null on error.
        }
    }
}