package com.talangraga.data.network.api

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val t: Exception) : Result<Nothing>
}
