package com.mindpalace.app.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MindBlog(
    @SerialName("id")
    val id: String,

    @SerialName("author_id")
    val authorId: String,

    @SerialName("description")
    val description: String,

    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content: String,

    @SerialName("publish_date")
    val publishDate: String,

    @SerialName("last_updated")
    val lastUpdated: String,
)