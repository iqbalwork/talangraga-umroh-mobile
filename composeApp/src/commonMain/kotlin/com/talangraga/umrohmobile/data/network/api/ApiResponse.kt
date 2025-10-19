package com.talangraga.umrohmobile.data.network.api

import kotlinx.serialization.Serializable

@Serializable
sealed class ApiResponse<out T, out E> {
    @Serializable
    data class Success<T>(val data: T) : ApiResponse<T, Nothing>()
    @Serializable
    data class Error<E>(val error: E) : ApiResponse<Nothing, E>()
}

