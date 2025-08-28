package com.talangraga.umrohmobile.data.mapper

import com.talangraga.umrohmobile.BuildKonfig
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.data.network.model.response.UserResponse

fun UserResponse.toUserEntity(): UserEntity {
    return UserEntity(
        userId = this.id ?: 0,
        userName = this.username.orEmpty(),
        fullname = this.fullname.orEmpty(),
        email = this.email.orEmpty(),
        phone = this.phone.orEmpty(),
        domisili = this.domisili.orEmpty(),
        userType = this.userType.orEmpty(),
        imageProfileUrl = "${BuildKonfig.BASE_URL.replace("/api/", "")}${this.imageProfile?.url}"
//        imageProfileUrl = if (BuildKonfig.BUILD_TYPE == "debug") {
//            "${
//                BuildConfig.STAGING_BASE_URL.replace(
//                    "/api/",
//                    ""
//                )
//            }${this.imageProfile?.url}"
//        } else {
//            "${
//                BuildConfig.PRODUCTION_BASE_URL.replace(
//                    "/api/",
//                    ""
//                )
//            }${this.imageProfile?.url}"
//        }
    )
}