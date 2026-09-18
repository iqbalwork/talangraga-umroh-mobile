package com.talangraga.data

/**
 * Public wrapper for [BuildKonfig] values to allow access from other modules.
 */
object AppConfig {
    val BASE_URL: String = BuildKonfig.BASE_URL
    val IS_DEBUG: Boolean = BuildKonfig.IS_DEBUG
}
