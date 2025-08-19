package com.talangraga.umrohmobile.constant

import com.talangraga.umrohmobile.shared.BuildConfig

fun isDebugBuild() = BuildConfig.BUILD_TYPE == "debug"
fun getBaseUrl(): String {
    return if (isDebugBuild()) BuildConfig.STAGING_BASE_URL else BuildConfig.PRODUCTION_BASE_URL
}