package com.mindpalace.app.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun highlightQueryInTitle(title: String, query: String): AnnotatedString {
    val startIndex = title.indexOf(query, ignoreCase = true)
    if (startIndex == -1) return AnnotatedString(title)

    val endIndex = startIndex + query.length

    return buildAnnotatedString {
        append(title.substring(0, startIndex))
        withStyle(
            style = SpanStyle(
                color = Color(0xFFEF6C00), // highlight color
                fontWeight = FontWeight.Bold
            )
        ) {
            append(title.substring(startIndex, endIndex))
        }
        append(title.substring(endIndex))
    }
}
