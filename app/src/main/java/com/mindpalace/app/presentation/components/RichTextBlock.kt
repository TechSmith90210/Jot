package com.mindpalace.app.presentation.components

import java.util.UUID

data class RichTextBlock(
    val id: String = UUID.randomUUID().toString(),
)