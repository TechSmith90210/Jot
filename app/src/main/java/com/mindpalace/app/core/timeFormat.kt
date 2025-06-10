package com.mindpalace.app.core
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

fun convertTimestampToHumanReadableFormat(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        .withZone(ZoneId.systemDefault())

    val instant = try {
        Instant.parse(timestamp) // works if it's full ISO-8601
    } catch (e: Exception) {
        // Fallback for date-only format like "2025-04-30"
        val localDate = java.time.LocalDate.parse(timestamp)
        localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
    }

    return formatter.format(instant)
}



fun formatCustomDateTime(input: String): String {
    if (input.isBlank()) return "Unknown"

    val flexibleFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd['T'][' ']HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true) // supports .SSS to .SSSSSSSSS
        .optionalEnd()
        .toFormatter()

    return try {
        val parsed = LocalDateTime.parse(input, flexibleFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
        parsed.format(outputFormatter)
    } catch (e: Exception) {
        "Invalid date"
    }
}

