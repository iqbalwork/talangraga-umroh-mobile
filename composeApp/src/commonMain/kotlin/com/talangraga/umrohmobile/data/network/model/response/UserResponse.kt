package com.talangraga.umrohmobile.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int? = 0, 
    val username: String? = null,
    val email: String? = null,
    val provider: String? = null,
    val confirmed: Boolean? = null,
    val blocked: Boolean? = null,
    val fullname: String? = null,
    val phone: String? = null,
    val domisili: String? = null,
    val userType: String? = null,
    val imageProfile: ImageResponse? = null
) : BaseResponse()