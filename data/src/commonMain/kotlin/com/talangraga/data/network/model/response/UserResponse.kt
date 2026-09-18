package com.talangraga.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMetadataResponse(
    @SerialName("username")
    val username: String? = null,
    @SerialName("fullname")
    val fullname: String? = null,
    @SerialName("phone_number")
    val phone: String? = null,
    @SerialName("domisili")
    val domisili: String? = null,
    @SerialName("user_type")
    val userType: String? = null,
    @SerialName("image_profile_url")
    val rawImageProfileUrl: String? = null,
    @SerialName("image_profile")
    val rawImageProfile: String? = null,
    @SerialName("avatar_url")
    val rawAvatarUrl: String? = null,
)

@Serializable
data class UserResponse(
    @SerialName("id")
    val id: String = "",
    @SerialName("username")
    val rawUsername: String? = null,
    @SerialName("fullname")
    val rawFullname: String? = null,
    @SerialName("email")
    val rawEmail: String? = null,
    @SerialName("phone_number")
    val rawPhone: String? = null,
    @SerialName("domisili")
    val rawDomisili: String? = null,
    @SerialName("user_type")
    val rawUserType: String? = null,
    @SerialName("image_profile_url")
    val rawImageProfileUrl: String? = null,
    @SerialName("image_profile")
    val rawImageProfile: String? = null,
    @SerialName("avatar_url")
    val rawAvatarUrl: String? = null,
    @SerialName("is_active")
    val isActive: Boolean? = null,
    @SerialName("user_metadata")
    val userMetadata: UserMetadataResponse? = null
) {
    val username: String?
        get() = rawUsername?.ifBlank { null } ?: userMetadata?.username

    val fullname: String?
        get() = rawFullname?.ifBlank { null } ?: userMetadata?.fullname

    val email: String?
        get() = rawEmail

    val phone: String?
        get() = rawPhone?.ifBlank { null } ?: userMetadata?.phone

    val domisili: String?
        get() = rawDomisili?.ifBlank { null } ?: userMetadata?.domisili

    val userType: String?
        get() = rawUserType?.ifBlank { null } ?: userMetadata?.userType

    val imageProfile: String?
        get() = rawImageProfileUrl?.ifBlank { null }
            ?: rawImageProfile?.ifBlank { null }
            ?: rawAvatarUrl?.ifBlank { null }
            ?: userMetadata?.rawImageProfileUrl?.ifBlank { null }
            ?: userMetadata?.rawImageProfile?.ifBlank { null }
            ?: userMetadata?.rawAvatarUrl?.ifBlank { null }
}
