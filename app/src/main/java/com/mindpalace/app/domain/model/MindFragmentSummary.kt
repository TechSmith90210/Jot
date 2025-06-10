package com.mindpalace.app.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MindFragmentSummary(
    @SerialName("id")
    val id: String,

    @SerialName("title")
    val title: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("last_opened_at")
    val lastOpenedAt: String
)