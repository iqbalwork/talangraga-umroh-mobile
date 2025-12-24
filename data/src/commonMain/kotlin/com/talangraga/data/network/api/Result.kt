package com.talangraga.data.network.api

sealed interface Result<out T> {
    data class Success<T>(val data: T) : com.talangraga.data.network.api.Result<T>
    data class Error(val t: Exception) : com.talangraga.data.network.api.Result<Nothing>
}
