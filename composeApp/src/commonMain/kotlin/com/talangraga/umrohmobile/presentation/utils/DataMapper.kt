package com.talangraga.umrohmobile.presentation.utils

import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

fun UserResponse.toUiData(): UserUIData {
    return UserUIData(
        id = id,
        username = username.orEmpty(),
        fullname = fullname.orEmpty(),
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        domicile = domisili.orEmpty(),
        userType = userType.orEmpty(),
        imageProfileUrl = imageProfile.orEmpty(),
        isActive = isActive ?: false,
    )
}

fun UserEntity.toUiData(): UserUIData {
    return UserUIData(
        id = userId,
        username = userName,
        fullname = fullname,
        email = email,
        phone = phone,
        domicile = domisili,
        userType = userType,
        imageProfileUrl = imageProfileUrl,
        isActive = true,
    )
}
