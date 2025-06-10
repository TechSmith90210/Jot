package com.mindpalace.app.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MindBlogWithUser(
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

    @SerialName("user") val user: UserInfo

)

@Serializable
data class UserInfo(
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_id") val avatarId: String
)


