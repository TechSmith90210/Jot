package com.mindpalace.app.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MindFragment(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("title")
    var title: String,

    @SerialName("content")
    var content: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("last_opened_at")
    val lastOpenedAt: String,

    @SerialName("last_updated")
    val lastUpdatedAt: String
)