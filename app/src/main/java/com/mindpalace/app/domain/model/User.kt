package com.mindpalace.app.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: String,

    @SerialName("email")
    val email: String,

    @SerialName("avatar_id")
    val avatarId: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("last_signed_in")
    val lastSignedIn : String,

    @SerialName("display_name")
    val displayName: String,

    @SerialName("bio")
    val bio: String?
)