package com.talangraga.umrohmobile.domain

import com.talangraga.umrohmobile.data.model.StrapiErrorResponse

sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error<T>(val message: String, val errorResponse: StrapiErrorResponse? = null) : Resource<T>
}