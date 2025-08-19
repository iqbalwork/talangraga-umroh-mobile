package com.talangraga.umrohmobile.domain

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val error: RootError) : Result<Nothing>
    object Loading : Result<Nothing>
}