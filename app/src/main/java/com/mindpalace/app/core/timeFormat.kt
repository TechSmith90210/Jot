package com.mindpalace.app.core
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun convertTimestampToHumanReadableFormat(timestamp: String): String {
    // Parse the timestamp to Instant
    val instant = Instant.parse(timestamp)

    // Define a formatter for a more human-readable format: "Jan 15, 2025"
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        .withZone(ZoneId.systemDefault()) // Use system default timezone

    // Format the timestamp
    return formatter.format(instant)
}