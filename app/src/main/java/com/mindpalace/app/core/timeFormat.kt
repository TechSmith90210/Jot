package com.mindpalace.app.core
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

    val patterns = listOf(
        "yyyy-MM-dd HH:mm:ss.SSSSSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss"
    )

    for (pattern in patterns) {
        try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val parsed = LocalDateTime.parse(input, formatter)
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
            return parsed.format(outputFormatter)
        } catch (_: Exception) {
        }
    }

    return "Invalid date"
}
