package com.talangraga.umrohmobile.domain

sealed interface RootError {
    object NetworkFailure : RootError
    data class ServerError(val message: String? = null) : RootError
    data class UnknownError(val throwable: Throwable) : RootError
}