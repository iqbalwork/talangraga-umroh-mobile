package com.talangraga.umrohmobile.presentation.user.detail

import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class UserDetailState(
    val user: UserUIData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface UserDetailEvent {
    data class GetUser(val userId: Int) : UserDetailEvent
    data object ClearError : UserDetailEvent
}

sealed interface UserDetailEffect {
    data class ShowToastError(val message: String) : UserDetailEffect
}
