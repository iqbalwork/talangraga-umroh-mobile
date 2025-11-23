package com.talangraga.data.network.api

import kotlinx.serialization.Serializable

@Serializable
sealed class ApiResponse<out T, out E> {
    @Serializable
    data class Success<T>(val data: T) : com.talangraga.data.network.api.ApiResponse<T, Nothing>()
    @Serializable
    data class Error<E>(val error: E) : com.talangraga.data.network.api.ApiResponse<Nothing, E>()
}

